package cn.liubinbin.pan.exceptions;

/**
 * Created by bin on 2019/4/18.
 */
public class BucketIsFullException extends PanException {
    public BucketIsFullException() {
        super();
    }

    public BucketIsFullException(final String message) {
        super(message);
    }

    public BucketIsFullException(final String message, final Throwable t) {
        super(message, t);
    }

    public BucketIsFullException(final Throwable t) {
        super(t);
    }
}
