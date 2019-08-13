# 问题

1. 一旦一个 byteBuffer 从 slab 返回之后，我们很难再次知道这个 bytebuffer 已经没有引用了，可以类似引入智能指针。ref https://mp.weixin.qq.com/s/p5NxvhpfYmoKMVnbAK6-zA
2. ByteArrayLinkedSlab 怎么去申请 deleteMarker 的 slot。可以引入一个 map 来充当 slot。