package cn.liubinbin.experiment.callableusage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author liubinbin
 *
 */
public class ExecutorsUsage {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> result = executorService.submit(new MyCallable());
		System.out.println("result: " + result.get());
		result = (Future<String>) executorService.submit(new MyRunnable());
		System.out.println("result: " + result.get());
		executorService.shutdownNow();
	}

}
