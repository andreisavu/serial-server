package serialserver.interfaces;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Generic serial port interface
 *
 */
public interface BaseSerialPort {

    public InputStream getInputStream();

    public OutputStream getOutputStream();

    public List<String[]> getPortsList();

    public boolean connect(String s, int i);

    public void close();
}
