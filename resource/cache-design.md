# 设计

参考 hashmap，paldb，bucketcache，mslab，memcache。

首先会分析目前上述的相关实现。



##paldb

一些代码分析可以参考https://github.com/liubinbin/src-reading.git里的markdown/paldb的内容。

### 缺点

* 只能写入一次。
* 不能更改

###可以借鉴的点

* index 部分用一个 slot 来包装，slot 是固定长度，可以按计算偏移量。
* 通过碰撞找数据，这个部分的实现也需要 slot 来保证。



## hashmap

### 缺点

* 可能gc问题，依赖的gc来处理内存问题。

###可以借鉴的点

* 计算 hash 值。



## bucketcache

### 缺点

* 主要用于文件。

###可以借鉴的点

* 有回收机制
* 按频次处理数据



## mslab

### 缺点



### 可以借鉴的点





## memcache



### 缺点



### 可以借鉴的点




























