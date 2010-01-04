package serialserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import serialserver.util.Latch;

/**
 * Server for serial port. Allow only one active connection at a given time.
 * 
 */
public class SerialStreamServer {

    private static Logger log = Logger.getLogger(SerialStreamServer.class.getName());
    private ServerSocket server;
    private InputStream serial_in;
    private OutputStream serial_out;

    public SerialStreamServer(int port, InputStream _serial_in, OutputStream _serial_out) throws IOException {
        log.info("Starting to listen on port " + port);
        server = new ServerSocket(port, 0);
        serial_in = _serial_in;
        serial_out = _serial_out;
    }

    public void run() throws IOException {
        while (true) {
            log.info("Waiting for client to connect.");
            Socket client = server.accept();
            handle(client);
        }
    }

    private void handle(Socket client) {
        log.info("Got client connection: " + client.getInetAddress().toString());
        try {
            Latch latch = new Latch(1);
            StreamConnector reader = createSerialReader(client, latch);
            StreamConnector writer = createSerialWriter(client, latch);

            latch.awaitZero();
            log.info("Pipe endpoint closed. Closing stream connectors.");

            exit(reader, writer);
            client.close();
            join(reader, writer);
        } catch (IOException ex) {
            log.severe(ex.getMessage());
        }
    }

    private void exit(StreamConnector... connectors) {
        for (StreamConnector c : connectors) {
            c.exit();
            c.interrupt();
        }
    }

    private void join(StreamConnector... connectors) {
        for (StreamConnector c : connectors) {
            try {
                c.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    private StreamConnector createSerialReader(Socket client, Latch latch)
            throws IOException {
        StreamConnector reader = new StreamConnector(latch,
                serial_in, client.getOutputStream(), /*debug=*/true, /*block=*/false);
        reader.start();
        return reader;
    }

    private StreamConnector createSerialWriter(Socket client, Latch latch)
            throws IOException {
        StreamConnector writer = new StreamConnector(latch,
                client.getInputStream(), serial_out, /*debug=*/true, /*block=*/true);
        writer.start();
        return writer;
    }
}
