
package serialserver.exceptions;

/**
 * Missing serial port error
 * 
 */
public class MissingPort extends SerialServerError {

    public MissingPort(String string) {
        super(string);
    }

}
