package com.test;

public class LinkedResizeableArray extends ResizeableArray<Object> {

	static class Node extends ResizeableArray.Node{
		int hash;
		Object data;
		Node left; 
		Node right;
		Node Nextlink;
	}
	private Node parent;
	LinkedResizeableArray(){
		parent= (Node) getArrayOfTrees()[0];
	}
	
	public Node put(Object key) {
		if (key == null)
			throw new NullPointerException("Key is null");
		ResizeableArray.Node[] arr = getArrayOfTrees();
		int hash = hash((int)key);
		int i = indexFor(hash, arr.length);
			if(parent!= null) parent.Nextlink= (Node) arr[i];
			else parent= (Node) arr[i];
		return parent;
	}
	public Node getFirstNode(){
		return parent;
	}
	
}
