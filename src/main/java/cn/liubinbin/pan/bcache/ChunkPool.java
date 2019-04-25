package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.exceptions.ChunkTooManyException;
import cn.liubinbin.pan.exceptions.DataTooBiglException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * Created by bin on 2019/4/24.
 */
public class ChunkPool {

    private Chunk[] chunksInPool;
    private int[] slotSizes;
    private int biggestSlotSize;
    private int chunkSize;
    private int chunkMaxCount;
    private AtomicInteger chunkCurrCount;

    public ChunkPool(Config cacheConfig) {
        this.slotSizes = cacheConfig.getSlotSizes();
        this.chunkSize = cacheConfig.getChunkSize();
        this.chunkMaxCount = cacheConfig.getChunkMaxCount();
        this.chunkCurrCount = new AtomicInteger(0);
        this.chunksInPool = new ByteArrayChunk[slotSizes.length];
        for (int chunkIdx = 0; chunkIdx < slotSizes.length; chunkIdx++) {
            this.chunksInPool[chunkIdx] = null;
        }
        this.biggestSlotSize = slotSizes[slotSizes.length - 1];
    }

    // TODO we should use datalen( keylength + valuelength)
    public Chunk allocate(int size) throws DataTooBiglException, ChunkTooManyException {
        if (chunkCurrCount.get() > chunkMaxCount){
            throw new ChunkTooManyException();
        }
        int choosenChunkIdx = chooseChunkIdx(size);
        if (choosenChunkIdx < 0) {
            throw new DataTooBiglException("size too big, biggestSlotSize is " + biggestSlotSize);
        }
//        Chunk = new Chunk(getSlotSizeForIdx(choosenChunkIdx), );
        return null;
    }

    public int chooseChunkIdx(int size) {
        for (int chunkIdx = 0; chunkIdx < slotSizes.length; chunkIdx++) {
            if (size < slotSizes[chunkIdx]) {
                return chunkIdx;
            }
        }
        return -1;
    }

    public int getSlotSizeForIdx(int idx) {
        return slotSizes[idx];
    }

}
