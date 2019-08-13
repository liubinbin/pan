package cn.liubinbin.pan.module;

/**
 * @author liubinbin
 */
public class Addr {

    private int slabIdx;
    private int offset;
    private int length;

    public Addr(int slabIdx, int offset, int length) {
        this.setSlabIdx(slabIdx);
        this.setOffset(offset);
        this.setLength(length);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSlabIdx() {
        return slabIdx;
    }

    public void setSlabIdx(int slabIdx) {
        this.slabIdx = slabIdx;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return String.format("{slabIdx: %d ; offset: %d ; length: %d }", slabIdx, offset, length);
    }
}
