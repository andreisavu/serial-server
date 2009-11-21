package serialserver.impl;

import java.util.logging.Logger;
import serialserver.interfaces.BaseSerialPort;
import serialserver.interfaces.SerialListener;

/**
 * Serial port adapter class factory
 *
 * Try rxtx and if it fails try sun javacomm
 */
public class SerialFactory {

    private static Logger log = Logger.getLogger(SerialFactory.class.getName());

    public static BaseSerialPort createInstance(SerialListener listener) {
        try {
            return createInstance("rxtx", listener);
        } catch(ClassNotFoundException e1) {
            log.info("rxtx serial driver not found, trying javacomm");
            try {
                return createInstance("sun", listener);
            } catch(ClassNotFoundException e2) {
                log.severe("javacomm driver not found. Giving up.");
            }
        }
        return null;
    }

    /**
     * Create a specific instance
     * 
     * @param platform
     * @param listener
     * @return
     * @throws ClassNotFoundException
     */
    public static BaseSerialPort createInstance(String platform, SerialListener listener) 
    throws ClassNotFoundException {
        if(platform.equals("rxtx")) {
            return new GNUSerialPort(platform, listener);
        } else if(platform.equals("sun")) {
            return new SunCommSerialPort(platform, listener);
        } else {
            throw new Error("Undefined platform");
        }
    }

}
