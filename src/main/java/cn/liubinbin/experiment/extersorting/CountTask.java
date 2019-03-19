package main.java.cn.liubinbin.experiment.extersorting;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author liubinbin
 *
 */

public class CountTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int THRESHOLD = 40;

	private int start;

	private int end;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		boolean canCompute = (end - start) <= THRESHOLD;
		if (canCompute) {
			for (int i = start; i <= end; i++) {
				sum += i;
			}
		} else {
			// ���������ڷ�ֵ���ͷ��ѳ��������������
			int mid = (start + end) / 2;
			CountTask leftTask = new CountTask(start, mid);
			CountTask rightTask = new CountTask(mid + 1, end);

			// ִ��������
			leftTask.fork();
			rightTask.fork();

			// �ȴ�������ִ���꣬���õ����
			int leftResult = (int) leftTask.join();
			int rightResult = (int) rightTask.join();

			sum = leftResult + rightResult;
		}

		return sum;
	}

	public static void main(String[] args) {
		ForkJoinPool forkJoinPool = new ForkJoinPool();

		// ����һ�������ʸ񣬸������1+2+3+4
		long startTime = System.currentTimeMillis();
		CountTask task = new CountTask(1, 1000000);
		Future<Integer> result = forkJoinPool.submit(task);
		try {
			System.out.println(result.get());
		} catch (Exception e) {
			System.out.println("abc");
		}
		System.out.println("time use " + (System.currentTimeMillis() - startTime) + " ms");
	}
}
