package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class SlabIsFullException extends PanException {
    public SlabIsFullException() {
        super();
    }

    public SlabIsFullException(final String message) {
        super(message);
    }

    public SlabIsFullException(final String message, final Throwable t) {
        super(message, t);
    }

    public SlabIsFullException(final Throwable t) {
        super(t);
    }
}
