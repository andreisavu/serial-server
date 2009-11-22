package serialserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Read from input stream and write to output stream 
 */
public class StreamConnector extends Thread {

    private static Logger log = Logger.getLogger(StreamConnector.class.getName());
    private InputStream in;
    private OutputStream out;
    private boolean debug;
    private final Object monitor;

    private volatile boolean should_exit = false;

    public StreamConnector(Object _monitor, InputStream _in, OutputStream _out, boolean _debug) {
        in = _in;
        out = _out;
        debug = _debug;
        monitor = _monitor;
    }

    @Override
    public void run() {
        try {
            log.info("Starting connector thread #" + getId());
            while (!should_exit) {
                if(in.available() > 0) {
                    int toRead = in.available();
                    byte[] data = new byte[toRead];

                    in.read(data, 0, toRead);
                    out.write(data);
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }
            log.info("Normal exit thread #" + getId());
        } catch (IOException ex) {
            log.severe("Thread #" + getId() + " " + ex.getMessage());
        }

        synchronized (monitor) {
            monitor.notify();   // The connector will exit. Notify main thread.
        }
    }

    public void exit() {
        should_exit = true;
    }
}
