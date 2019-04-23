/**
 * this package is the first version of cache impelement.
 * 1. KeyValue is arranged by valueLen
 * 2. we cannot reuse the space occupied by key after being deleted
 * 3. put key and addr in concurrentskiplist
 * Created by bin on 2019/4/23.
 */
package cn.liubinbin.pan.oldcache;