package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class DataTooBiglException extends PanException {
    public DataTooBiglException() {
        super();
    }

    public DataTooBiglException(final String message) {
        super(message);
    }

    public DataTooBiglException(final String message, final Throwable t) {
        super(message, t);
    }

    public DataTooBiglException(final Throwable t) {
        super(t);
    }
}
