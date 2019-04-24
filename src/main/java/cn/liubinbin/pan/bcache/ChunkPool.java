package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;

/**
 * Created by bin on 2019/4/24.
 */
public class ChunkPool {

    private Chunk[] chunks;
    private int[] bucketSlotSize;

    public ChunkPool(Config cacheConfig) {
        this.bucketSlotSize = cacheConfig.getBucketSlotSize();
        this.chunks = new ByteArrayChunk[bucketSlotSize.length];
        for (int bucketIdx = 0; bucketIdx < bucketSlotSize.length; bucketIdx++) {
            this.chunks[bucketIdx] = new ByteArrayChunk(bucketSlotSize[bucketIdx], cacheConfig.getChunkSize());
        }
    }

    public Chunk allocate(int size) {
        return null;
    }


}
