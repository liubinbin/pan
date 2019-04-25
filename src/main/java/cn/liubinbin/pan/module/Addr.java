package cn.liubinbin.pan.module;

/**
 * @author liubinbin
 */
public class Addr {

    private int chunkIdx;
    private int offset;
    private int length;

    public Addr(int chunkIdx, int offset, int length) {
        this.setChunkIdx(chunkIdx);
        this.setOffset(offset);
        this.setLength(length);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getChunkIdx() {
        return chunkIdx;
    }

    public void setChunkIdx(int chunkIdx) {
        this.chunkIdx = chunkIdx;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return String.format("{chunkIdx: %d ; offset: %d ; length: %d }", chunkIdx, offset, length);
    }
}
