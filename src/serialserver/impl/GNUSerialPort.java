package serialserver.impl;

import serialserver.interfaces.BaseSerialPort;
import serialserver.interfaces.SerialListener;
import gnu.io.CommDriver;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 */
public class GNUSerialPort implements SerialPortEventListener, BaseSerialPort {

    private static Logger log = Logger.getLogger(GNUSerialPort.class.getName());

    private InputStream in;
    private OutputStream out;
    private SerialPort serialPort = null;
    private SerialListener parent;

    public GNUSerialPort(String _drivername, SerialListener _parent) {
        parent = _parent;
        if (_drivername == null) {
            _drivername = "gnu.io.RXTXCommDriver";
        }
        try {
            CommDriver driver = (CommDriver) Class.forName(_drivername).newInstance();
            driver.initialize();
        } catch (Exception e) {
            log.severe("Class not found:" + _drivername);
        }
    }

    public OutputStream getOutputStream() {
        return out;
    }

    public InputStream getInputStream() {
        return in;
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

    public void serialEvent(SerialPortEvent _e) {

        if (_e.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                while (in.available() > 0) {
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);
                    parent.gotFromSerial(buffer);
                }
            } catch (IOException e) {
                System.out.println("Trouble with the serial event handler. " + e);
            }
        }
    }

    public boolean connect(String whichPort, int whichSpeed) {
        log.info("Trying to connect to " + whichPort + " at " + whichSpeed);
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
                log.severe("Serial stream problem");
                return false;
            }

        } catch (Exception e) {
            log.severe("Port " + whichPort + " in Use " + e);
            return false;
        }

        return true;
    }

    public void close() {
        serialPort.close();
    }
}
