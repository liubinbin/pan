package cn.liubinbin.experiment.topk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * find topk (biggest) from a dataset
 * @author liubinbin
 * @NotThreadSafe
 */
public class Topk {

	private int NONE_SON = -1;
	private int capacity;
	int[] dataSet;
	int size;
	int firstIdx;
	
	public Topk(int capacity) {
		this.capacity = capacity;
		this.dataSet = new int[capacity];
		this.size = 0;
		this.firstIdx = 0;
	}
	
	public Topk(int capacity, int[] initialData) {
		this.capacity = capacity;
		this.dataSet = new int[capacity];
	}
	
	public Topk(int capacity, ArrayList<Integer> initialData) {
		this.capacity = capacity;
		this.dataSet = new int[capacity];
	}
	
	public void add(int number) {
		if (size < capacity) {
			dataSet[size] = number;
			size++;
			if (size == capacity) {
				initHeap();
			}
		} else if (dataSet[firstIdx] > number ) {
			//discard number
			return ;
		} else if (dataSet[firstIdx] < number ) {
			dataSet[firstIdx] = number;
			adjust(firstIdx);
		}
	}
	
	public void initHeap() {
		for (int idx = capacity - 1; idx >= 0; idx--) {
			adjust(idx);
		}
	}
	
	public int findLSon(int idx) {
		int lParent = 2 * idx + 1;
		if (lParent > capacity -1) {
			return NONE_SON;
		} else {
			return lParent;
		}
	}
	
	public int findRSon(int idx) {
		int lParent = 2 * idx + 2;
		if (lParent > capacity -1) {
			return NONE_SON;
		} else {
			return lParent;
		}
	}
	
	public void adjust(int idx ) {
		int lSon = findLSon(idx);
		int rSon = findRSon(idx);
		if (lSon == NONE_SON && rSon == NONE_SON) {
			//hasn't lSon, hasn't rSon
			return;
		} else if (lSon != NONE_SON && rSon == NONE_SON) {
			//has lSon, hasn't rSon
			if (dataSet[lSon] < dataSet[idx]) {
				swap(lSon, idx);
				adjust(lSon);
			}
		} else if (lSon != NONE_SON && rSon != NONE_SON) {
			//has lSon, has rSon
			if (dataSet[lSon] < dataSet[rSon]) {
				//lSon is smaller
				if (dataSet[lSon] < dataSet[idx]) {
					swap(lSon, idx);
					adjust(lSon);
				}
			} else {
				//rSon is smaller
				if (dataSet[rSon] < dataSet[idx]) {
					swap(rSon, idx);
					adjust(rSon);
				}
			}
		}
	}
	
	public void swap(int idxa, int idxb) {
		int temp = dataSet[idxa];
		dataSet[idxa] = dataSet[idxb];
		dataSet[idxb] = temp;
	}
	
	public void printMaxHeap() {
		System.out.print("data: ");
		int sum = 0;
		for (int idx = 0; idx < size; idx++) {
			System.out.print(dataSet[idx] + " ");
			sum += dataSet[idx];
		}
		System.out.println();
		System.out.println("Topk.sum " + sum);
	}
	
	public static void main(String[] args) {
		Topk maxHeap = new Topk(10);
		int numCount = 10000;
		Random random = new Random();
		List<Integer> tempDataSet = new ArrayList<>();
		for( int i = 0; i < numCount; i++ ) {
			int tempNum = random.nextInt(10000);
			System.out.println("tempNum " + tempNum);
			tempDataSet.add(tempNum);
			maxHeap.add(tempNum);
		}
		Collections.sort(tempDataSet);
		int tempDataSetSum = 0;
		System.out.print("tempDataSet sorted: ");
		for(int idx = numCount - 10; idx < numCount; idx++) {
			System.out.print(tempDataSet.get(idx) + " ");
			tempDataSetSum += tempDataSet.get(idx);
		}
		System.out.print("\ntempDataSet.sorted.sum: " + tempDataSetSum);
		System.out.println();
		maxHeap.printMaxHeap();

	}

}
