/*
 * Application entry point
 */
package serialserver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Application entry class. Parse command line arguments and init server.
 * 
 */
public class Main {

    private static Logger log = Logger.getLogger(Main.class.toString());

    public static void main(String[] args) {
        try {
            log.info("Parsing command line arguments.");

            Config config = parseCommandLine(args);
            log.info(config.toString());

            startServer(config);

        } catch(IllegalArgumentException ex) {
            log.severe(ex.getMessage());
            return;
        }
    }

    private static Config parseCommandLine(String[] args) {
        OptionParser parser = new OptionParser() {
            {
                accepts("serial-port", "serial port").withRequiredArg();
                accepts("listen-port", "server listen port").withRequiredArg().ofType(Integer.class);
                accepts("debug", "display debug info");
                acceptsAll(asList("h", "help", "?"), "show help");
            }
        };

        OptionSet options = parser.parse(args);
        if(options.has("?")) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Config config = new Config();
        if(options.hasArgument("serial-port")) {
            config.setSerialPort(String.valueOf(options.valueOf("serial-port")));
        } else {
            throw new IllegalArgumentException("Expecting serial port as a command line argument.");
        }
        if(options.hasArgument("listen-port")) {
            config.setServerPort(Integer.parseInt(String.valueOf(options.valueOf("listen-port"))));
        } else {
            throw new IllegalArgumentException("Expecting listen port as a command line argument.");
        }
        if(options.has("debug")) {
            config.setDebug(true);
        }

        return config;
    }

    private static List<String> asList(String ... args) {
        List<String> result = new LinkedList<String>();
        for(String arg : args) {
            result.add(arg);
        }
        return result;
    }

    private static void startServer(Config config) {
        log.info("Starting server and connecting to port.");
        
    }
}
