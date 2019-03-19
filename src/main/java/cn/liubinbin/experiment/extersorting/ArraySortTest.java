package main.java.cn.liubinbin.experiment.extersorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author liubinbin
 *
 */
public class ArraySortTest {

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		list.add(2);
		list.add(1);
		Collections.sort(list);
		for (Integer item : list) {
			System.out.println("list.item " + item);
		}

		Set<Integer> set = new TreeSet<Integer>();
		set.add(1);
		set.add(3);
		set.add(2);
		set.add(1);
		System.out.println(set);
	}

}
