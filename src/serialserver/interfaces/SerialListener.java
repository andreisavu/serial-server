package serialserver.interfaces;


/**
 * Generic Serial event listener
 */
public interface SerialListener {
    public void gotFromSerial(byte[] _inputByteArray);
}
