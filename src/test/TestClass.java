package com.api.test;

public class TestClass {

	public static void main(String[] args) {
		
		MyHashMap hashMap = new MyHashMap();
		hashMap.put("fsdf", "as");
		
		System.out.println("TestClass.main() "+ hashMap);
		System.out.println("\n");
		
		MyLinkedHashMap map = new MyLinkedHashMap();
		map.put("fsdf", "as");
		
		System.out.println("TestClass.main() "+ map);
		System.out.println("\n");
		
		MyHashSet<String> set = new MyHashSet<String>();
		set.add("HI");
		set.add("HsdI");
		
		System.out.println("TestClass.main()"+ set);
		System.out.println("\n");
		
		MyLinkedHashSet lset = new MyLinkedHashSet();
		lset.add("HI");
		lset.add("HsdI");
		
		System.out.println("TestClass.main()"+ lset);
		
	}

}
