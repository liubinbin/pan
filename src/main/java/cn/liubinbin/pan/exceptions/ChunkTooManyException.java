package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class ChunkTooManyException extends PanException {
    public ChunkTooManyException() {
        super("we already allocate too many chunks");
    }

    public ChunkTooManyException(final String message) {
        super(message);
    }

    public ChunkTooManyException(final String message, final Throwable t) {
        super(message, t);
    }

    public ChunkTooManyException(final Throwable t) {
        super(t);
    }
}
