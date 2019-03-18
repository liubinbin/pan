package main.java.cn.liubinbin.experiment.hedged.v2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *			1000	
 * 	get		150762
 * 	hedget	
 * @author liubinbin
 *
 */
public class HedgedClient {

	private Random random;
	private final long hedgedReadThresholdMills = 30;
	private final int hedgedReadThreadpoolSize = 5;
	private static ThreadPoolExecutor hedgedReadThreadpool;

	public HedgedClient() {
		this.random = new Random();
		if (hedgedReadThreadpoolSize > 0) {
			hedgedReadThreadpool = new ThreadPoolExecutor(1, hedgedReadThreadpoolSize, 60, TimeUnit.SECONDS,
					new SynchronousQueue<Runnable>(), new ThreadFactory() {
						private final AtomicInteger threadIndex = new AtomicInteger(0);

						@Override
						public Thread newThread(Runnable r) {
							Thread t = new Thread(r);
							t.setName("hedgedRead-" + threadIndex.getAndIncrement());
							return t;
						}
					}, new ThreadPoolExecutor.CallerRunsPolicy() {

						@Override
						public void rejectedExecution(Runnable runnable, ThreadPoolExecutor e) {
							System.out.println("Execution rejected, Executing in current thread");
							// will run in the current thread
							super.rejectedExecution(runnable, e);
						}
					});
			hedgedReadThreadpool.allowCoreThreadTimeOut(true);
		}
	}

	public void get(int idx) {
		System.out.println("idx " + idx);
		if (idx % 2 == 0 ) {
			int sleeTimeInMs = 100 + random.nextInt(100);
			System.out.println("sleeTimeInMs: " + sleeTimeInMs);
			try {
				Thread.sleep(sleeTimeInMs);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("sleeTimeInMs: 0, do not sleep ");
		}
	}

	public Callable<String> getCallable(final int idx) {
		return new Callable<String>() {
		      @Override
		      public String call() throws Exception {
		        get(idx);
		        return "a";
		      }
		    };
	}

	private String getFirstToComplete(CompletionService<String> hedgedService)
			throws ExecutionException, InterruptedException {
		Future<String> result = hedgedService.take();
		return result.get();
	}

	private void cancelAll(ArrayList<Future<String>> futures) {
		for (Future<String> future : futures) {
			// Unfortunately, hdfs reads do not take kindly to interruption.
			// Threads return a variety of interrupted-type exceptions but
			// also complaints about invalid pbs -- likely because read
			// is interrupted before gets whole pb. Also verbose WARN
			// logging. So, for now, do not interrupt running read.
			future.cancel(false);
		}
	}

	public void hedgedGet(int idx) {
		ArrayList<Future<String>> futures = null;
		CompletionService<String> hedgedService = new ExecutorCompletionService<>(hedgedReadThreadpool);
		while (true) {
			if (futures == null) {
				Callable<String> getFromDataNodeCallable = getCallable(idx++);
				Future<String> firstRequest = hedgedService.submit(getFromDataNodeCallable);
				futures = new ArrayList<Future<String>>();
				futures.add(firstRequest);
				try {
					Future<String> result = hedgedService.poll(hedgedReadThresholdMills, TimeUnit.MILLISECONDS);
					if (result != null) {
						return;
					}
				} catch (InterruptedException e) {
					
				}
			} else {
				Callable<String> getFromDataNodeCallable = getCallable(idx++);
				Future<String> secondRequest = hedgedService.submit(getFromDataNodeCallable);
				futures.add(secondRequest);
				try {
					String result = getFirstToComplete(hedgedService);
					// cancel the rest.
					cancelAll(futures);
					return;
				} catch (ExecutionException e) {
					
				} catch (InterruptedException e) {
					
				}
				
			}
		}
	}

	public static void main(String[] args) {
		HedgedClient hedgedClient = new HedgedClient();
		long startTime = System.currentTimeMillis();
		for (int idx = 0; idx < 1; idx++) {
			long startTimeOneRound = System.currentTimeMillis();
//			hedgedClient.get(idx);
			hedgedClient.hedgedGet(idx);
			System.out.println("one round " + (System.currentTimeMillis() - startTimeOneRound) + " ms");
		}
		System.out.println("used time " + (System.currentTimeMillis() - startTime) + " ms");
		hedgedReadThreadpool.shutdown();
	}

}
