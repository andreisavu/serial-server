package serialserver;

/**
 * Basic server configuration params
 */
public class Config {

    private String serialPort;
    private int serialSpeed = 9600;
    private int serverPort = 9000;
    private boolean debug = false;

    public int getSerialSpeed() {
        return serialSpeed;
    }

    public void setSerialSpeed(int serialSpeed) {
        this.serialSpeed = serialSpeed;
    }

    public void setSerialSpeed(String serialSpeed) {
        this.serialSpeed = Integer.parseInt(serialSpeed);
    }

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

    public void setServerPort(String serverPort) {
        this.serverPort = Integer.parseInt(serverPort);
    }

    @Override
    public String toString() {
        return "<SerialPort:" + serialPort + ", SerialSpeed: " + serialSpeed +
                ", ServerPort:" + serverPort + " Debug:" + debug + ">";
    }
}
