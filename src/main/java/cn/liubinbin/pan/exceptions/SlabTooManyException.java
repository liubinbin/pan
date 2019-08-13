package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class SlabTooManyException extends PanException {
    public SlabTooManyException() {
        super("we already allocate too many chunks");
    }

    public SlabTooManyException(final String message) {
        super(message);
    }

    public SlabTooManyException(final String message, final Throwable t) {
        super(message, t);
    }

    public SlabTooManyException(final Throwable t) {
        super(t);
    }
}
