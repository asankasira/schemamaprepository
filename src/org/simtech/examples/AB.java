package org.simtech.examples;

import java.util.*;

public class AB {

	public static void main(String[] args) {
		
		List<String> lst = new ArrayList<String>();
		lst.add("A");
		lst.add("B");
		lst.add("C");
		lst.add("D");
		lst.add("E");
		lst.add("F");
		
		for(Iterator<String> lt = lst.listIterator(); lt.hasNext();){
			
			String s = lt.next();
			
			if("D".compareTo(s) > 0)
				lt.remove();
		}
		
		System.out.println(lst);
	}
}
