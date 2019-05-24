package cn.liubinbin.pan.exceptions;

/**
 *
 * Created by bin on 2019/4/18.
 */
public class SlotBiggerThanChunkException extends PanException {
    public SlotBiggerThanChunkException() {
        super("slotSize is bigger than chunkSize");
    }

    public SlotBiggerThanChunkException(final String message) {
        super(message);
    }

    public SlotBiggerThanChunkException(final String message, final Throwable t) {
        super(message, t);
    }

    public SlotBiggerThanChunkException(final Throwable t) {
        super(t);
    }
}
