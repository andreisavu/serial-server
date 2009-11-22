package serialserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Server for serial port. Allow only one active connection at a given time.
 * 
 */
public class StreamServer {

    private static Logger log = Logger.getLogger(StreamServer.class.getName());
    private ServerSocket server;
    private InputStream in;
    private OutputStream out;

    public StreamServer(int port, InputStream _in, OutputStream _out) throws IOException {
        log.info("Starting to listen on port " + port);
        server = new ServerSocket(port, 0);
        in = _in;
        out = _out;
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
            synchronized (this) {
                StreamConnector reader = createReader(client);
                StreamConnector writer = createWriter(client);

                try {
                    wait();
                } catch (InterruptedException ex) {
                }

                exit(reader, writer);
                client.close();
            }
        } catch (IOException ex) {
            log.severe(ex.getMessage());
        }
    }

    private void exit(StreamConnector... connectors) {
        for (StreamConnector c : connectors) {
            c.exit();
        }
    }

    private StreamConnector createReader(Socket client)
            throws IOException {
        StreamConnector reader = new StreamConnector(this,
                in, client.getOutputStream(), true);
        reader.start();
        return reader;
    }

    private StreamConnector createWriter(Socket client)
            throws IOException {
        StreamConnector writer = new StreamConnector(this,
                client.getInputStream(), out, true);
        writer.start();
        return writer;
    }
}