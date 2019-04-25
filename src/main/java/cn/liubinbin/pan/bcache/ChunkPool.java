package cn.liubinbin.pan.bcache;

import cn.liubinbin.pan.conf.Config;

/**
 *
 * Created by bin on 2019/4/24.
 */
public class ChunkPool {

    private Chunk[] chunks;
    private int[] chunkSlotSize;

    public ChunkPool(Config cacheConfig) {
        this.chunkSlotSize = cacheConfig.getChunkSlotSize();
        this.chunks = new ByteArrayChunk[chunkSlotSize.length];
        for (int chunkIdx = 0; chunkIdx < chunkSlotSize.length; chunkIdx++) {
            this.chunks[chunkIdx] = null;
        }
    }

    public Chunk allocate(int size) {
        return null;
    }


}
