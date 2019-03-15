package main.java.cn.liubinbin.pan.experiment.maxheap;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author liubinbin
 * @NotThreadSafe
 */
public class MaxHeap {

	private int capacity;
	int[] dataSet;
	
	public MaxHeap(int capacity) {
		this.capacity = capacity;
		this.dataSet = new int[capacity];
	}
	
	public MaxHeap(int capacity, int[] initialData) {
		this.capacity = capacity;
		this.dataSet = new int[capacity];
	}
	
	public MaxHeap(int capacity, ArrayList<Integer> initialData) {
		this.capacity = capacity;
		this.dataSet = new int[capacity];
	}
	
	public void add(int number) {
		
	}
	
	
	public ArrayList<Integer> getResult(){
		
		return null;
	}
	
	public static void main(String[] args) {
		Random random = new Random();
		for(int i = 0; i < 1000; i++) {
			System.out.println(random.nextInt(100));
		}
		
		
	}

}
