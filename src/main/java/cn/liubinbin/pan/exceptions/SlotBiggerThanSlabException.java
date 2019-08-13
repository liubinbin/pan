package cn.liubinbin.pan.exceptions;

/**
 * Created by bin on 2019/4/18.
 */
public class SlotBiggerThanSlabException extends PanException {
    public SlotBiggerThanSlabException() {
        super("slotSize is bigger than slabSize");
    }

    public SlotBiggerThanSlabException(final String message) {
        super(message);
    }

    public SlotBiggerThanSlabException(final String message, final Throwable t) {
        super(message, t);
    }

    public SlotBiggerThanSlabException(final Throwable t) {
        super(t);
    }
}
