# hbase-wal







```
doMiniBatchMutate
	sync(txid, durability);
	
  // Sync all known transactions
  private void publishSyncThenBlockOnCompletion(TraceScope scope, boolean forceSync) throws IOException {
    SyncFuture syncFuture = publishSyncOnRingBuffer(forceSync);
    blockOnSync(syncFuture);
  }
  
  
  syncFuture.get(walSyncTimeoutNs);
  
  synchronized long get(long timeoutNs) throws InterruptedException,
      ExecutionException, TimeoutIOException {
    final long done = System.nanoTime() + timeoutNs;
    while (!isDone()) {
      wait(1000);
      if (System.nanoTime() >= done) {
        throw new TimeoutIOException(
            "Failed to get sync result after " + TimeUnit.NANOSECONDS.toMillis(timeoutNs)
                + " ms for txid=" + this.txid + ", WAL system stuck?");
      }
    }
    if (this.throwable != null) {
      throw new ExecutionException(this.throwable);
    }
    return this.doneTxid;
  }
  
  synchronized boolean isDone() {
    return this.doneTxid != NOT_DONE;
  }
  
  以上为的handler卡住的情况，需要如下done的去修改doneTxid
  
  protected SyncFuture publishSyncOnRingBuffer(long sequence, boolean forceSync) {
    // here we use ring buffer sequence as transaction id
    SyncFuture syncFuture = getSyncFuture(sequence).setForceSync(forceSync);
    try {
      RingBufferTruck truck = this.disruptor.getRingBuffer().get(sequence);
      truck.load(syncFuture);
    } finally {
      this.disruptor.getRingBuffer().publish(sequence);
    }
    return syncFuture;
  }
  
    synchronized boolean done(final long txid, final Throwable t) {
    if (isDone()) {
      return false;
    }
    this.throwable = t;
    if (txid < this.txid) {
      // Something badly wrong.
      if (throwable == null) {
        this.throwable =
            new IllegalStateException("done txid=" + txid + ", my txid=" + this.txid);
      }
    }
    // Mark done.
    this.doneTxid = txid;
    // Wake up waiting threads.
    notify();
    return true;
  }
  
  
 调用的以上的done函数，需要。
     private int releaseSyncFuture(final SyncFuture syncFuture, final long currentSequence,
        final Throwable t) {
      if (!syncFuture.done(currentSequence, t)) {
        throw new IllegalStateException();
      }

      // This function releases one sync future only.
      return 1;
    }
    
    syncRunner中调用
    while (true) {排除一些多余向前进
     writer.sync(useHsync);
     syncCount += releaseSyncFuture(takeSyncFuture, currentHighestSyncedSequence, null);
     
    
    ringbuffer的onEvent 里调用，append(entry);将entry加入append入writer里。然后将sequence的加入syncRunner中，如下函数
    this.syncRunners[this.syncRunnerIndex].offer(sequence, this.syncFutures,
              this.syncFuturesCount.get());
              
    多个syncfutures通过highestSyncedTxid同步已经写入txid。
    
    
    this.syncFutures.take() 都会对应一次releaseSyncFuture。
```



```
if (truck.type() == RingBufferTruck.Type.SYNC) {
  this.syncFutures[this.syncFuturesCount.getAndIncrement()] = truck.unloadSync();
  // Force flush of syncs if we are carrying a full complement of syncFutures.
  if (this.syncFuturesCount.get() == this.syncFutures.length) {
    endOfBatch = true;
  }
} else if (truck.type() == RingBufferTruck.Type.APPEND) {
```



ringbuffer传递内容包括的sync标志（主要用于传递SyncFuture）和数据。分开可以用于控制。



主流程：

1. handler将entry和txid写入disruptor，然后通过sync函数等待，通过一个threadlocal的设置了txid的SyncFuture，调用get方法阻塞。
2. 会把SyncFuture和Sync标志写入到disruptor中。
3. 在disruptor的Handler的onEvent里：
   1. 将entry给append到writer里。
   2. 设置SyncFuture，用于传递。
4. 给syncRunners传递带txid的SyncFuture
5. SyncRunner会在while里不停的跑，
   1. releaseSyncFuture已经sync过的数据（可能在别的syncRunner）
   2. sync数据，然后releaseSyncFuture




此处需要一张图



思考：syncRunner为多个，大概是为了隔离notifier和sync，两种操作不要在一起，最终可以减轻同步代价。











