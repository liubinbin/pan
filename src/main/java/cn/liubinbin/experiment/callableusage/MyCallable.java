package cn.liubinbin.experiment.callableusage;

import java.util.concurrent.Callable;

/**
 *
 * @author liubinbin
 *
 */
public class MyCallable implements Callable<String> {

	@Override
	public String call() throws Exception {
		System.out.println("execute in MyCallable");
		return "MyCallable's result";
	}

}
