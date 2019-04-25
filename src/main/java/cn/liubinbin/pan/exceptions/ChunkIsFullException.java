package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class ChunkIsFullException extends PanException {
    public ChunkIsFullException() {
        super();
    }

    public ChunkIsFullException(final String message) {
        super(message);
    }

    public ChunkIsFullException(final String message, final Throwable t) {
        super(message, t);
    }

    public ChunkIsFullException(final Throwable t) {
        super(t);
    }
}
