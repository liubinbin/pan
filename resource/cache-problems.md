# 问题

1. 一旦一个byteBuffer从chunk返回之后，我们很难再次知道这个bytebuffer已经没有引用了，可以类似引入智能指针。ref https://mp.weixin.qq.com/s/p5NxvhpfYmoKMVnbAK6-zA
2. ByteArrayLinkedChunk怎么去申请deleteMarker的slot。可以引入一个map来充当slot。