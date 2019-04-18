package cn.liubinbin.pan.exceptions;

/**
 * Created by bin on 2019/4/18.
 */
public class PanException extends Exception {
    public PanException() {
        super();
    }

    public PanException(final String message) {
        super(message);
    }

    public PanException(final String message, final Throwable t) {
        super(message, t);
    }

    public PanException(final Throwable t) {
        super(t);
    }
}
