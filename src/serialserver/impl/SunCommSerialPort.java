package serialserver.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.comm.CommDriver;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import serialserver.interfaces.BaseSerialPort;
import serialserver.interfaces.SerialListener;

/**
 *
 * @author andrei
 */
public class SunCommSerialPort implements SerialPortEventListener, BaseSerialPort {

    private static Logger log = Logger.getLogger(SunCommSerialPort.class.getName());

    private InputStream in;
    private OutputStream out;
    private SerialPort serialPort = null;
    private SerialListener parent;

    SunCommSerialPort(String _drivername, SerialListener _parent) throws ClassNotFoundException {
        parent = _parent;
        if (_drivername == null) {
            _drivername = "com.sun.comm.Win32Driver";
        }
        try {
            CommDriver driver = (CommDriver) Class.forName(_drivername).newInstance();
            driver.initialize();
        } catch (InstantiationException ex) {
            log.severe(ex.toString());
            throw new Error(ex);
        } catch (IllegalAccessException ex) {
            log.severe(ex.toString());
            throw new Error(ex);
        }
    }

    public List<String[]> getPortsList() {
        List<String[]> ports = new ArrayList<String[]>();
        CommPortIdentifier portId;

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                String[] portAndOwner = {portId.getName(), portId.getCurrentOwner()};
                ports.add(portAndOwner);
            }
        }
        return ports;
    }

    public OutputStream getOutputStream() {
        return out;
    }


    public InputStream getInputStream() {
        return in;
    }

    public boolean connect(String whichPort, int whichSpeed) {
        log.info("Trying to connecto to " + whichPort + " at " + whichSpeed);
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(whichPort);
            serialPort = (SerialPort) portId.open("SerialServer " + whichPort, 2000);
            try {
                serialPort.setSerialPortParams(
                        whichSpeed,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException e) {
                log.severe("Can't set params");
                return false;
            }
            try {
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                if (parent != null) {
                    serialPort.addEventListener(this);
                    serialPort.notifyOnDataAvailable(true);
                }
            } catch (IOException e) {
                log.severe("Serial Stream Problem. Couldn't get streams");
                return false;
            }

        } catch (Exception e) {
            log.severe("Port " + whichPort + " in Use " + e);
            return false;
        }

        return true;
    }

    public void serialEvent(SerialPortEvent _e) {

        if (_e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                while (in.available() > 0) {
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);
                    parent.gotFromSerial(buffer);
                }
            } catch (IOException e) {
                log.severe("Trouble with the serial event handler. " + e);
            }
        }
    }

    public void close() {
        serialPort.close();
        serialPort = null;
    }

}
