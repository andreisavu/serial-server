package serialserver;

/**
 * Basic server configuration params
 */
public class Config {

    private String serialPort;
    private int serverPort = 9000;
    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String toString() {
        return "<SerialPort:" + serialPort + ", ServerPort:"
                + serverPort + " Debug:" + debug + ">";
    }
}
