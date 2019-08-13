package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class TooManySlabsException extends PanException {
    public TooManySlabsException() {
        super("we already allocate too many slabs");
    }

    public TooManySlabsException(final String message) {
        super(message);
    }

    public TooManySlabsException(final String message, final Throwable t) {
        super(message, t);
    }

    public TooManySlabsException(final Throwable t) {
        super(t);
    }
}
