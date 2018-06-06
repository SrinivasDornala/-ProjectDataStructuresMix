package com.test;

public class ResizeableArray<T> {
	
	@SuppressWarnings("unchecked")
	ResizeableArray(){
		this.arr = new Node[10];
	}
	private Node arr[];
	private Node temp;

	public Node[] getArrayOfTrees() {
		return this.arr;
	}

	@SuppressWarnings("unchecked")
	Node[] resizeArray(){
		if(this.arr.length==0) this.arr = new Node[10];
		int len = this.arr.length;
		Node[] a =  new Node[2*len];
		System.arraycopy(arr, 0, a, 0, len-1);
		this.arr =  a;
		return arr;
	}
	
	static class Node{
		int hash;
		Object data;
		Node left;
		Node right;
	}
	
	static int hash(int h) {
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	public Node get(Object key) {
		if (key == null)
			throw new NullPointerException("Key is null");
		int hash = hash(key.hashCode());
		traversal(arr[indexFor(hash, arr.length)],key,hash);
		return temp;
	}
	
	void traversal(Node root , Object key,int hash){
		if(root!= null){
			Object k= root.data;
			if (root.hash == hash && (k == key || key.equals(k)))
				temp= root;
			traversal(root.left, key, hash);
			traversal(root.right, key, hash);
		}
	}

	public Node put(Object key) {
		if (key == null)
			throw new NullPointerException("Key is null");
		int hash = hash(key.hashCode());
		int i = indexFor(hash, arr.length);
		if(i>arr.length) resizeArray();
		traversal(arr[i],key,hash);
		if (temp!=null) {
			insertRec(arr[i], key);
		}else{
			arr[i]= new Node();
			arr[i].data= key;
		}
		temp = null;
		return null;
	}

	Node insertRec(Node root, Object key) {
		if (root == null) {
			root = new Node();
			root.data= key;
			return root;
		}

		if ((int)key < (int)root.data)
			root.left = insertRec(root.left, key);
		else if ((int)key > (int)root.data)
			root.right = insertRec(root.right, key);

		return root;
	}

	final Node remove(Object key) {
		if (key == null)
			throw new NullPointerException("Key is null");
		int hash = hash(key.hashCode());
		int i = indexFor(hash, arr.length);
		Node root = arr[i];
		Node res;
		if (root != null) {
			res = deleteNode(root, (int)key);
		}
		return null;
	}
	private Node minValueNode(Node node){
	    Node current = node;
	    while (current.left != null) current = current.left;
	    return current;
	}
	 
	private Node deleteNode(Node root, int key){
	    if (root == null) return root;
	    // If the key to be deleted is smaller than the root's key,
	    // then it lies in left subtree
	    if ((int)key < (int)root.data) root.left = deleteNode(root.left, key);
	    else if ((int)key > (int)root.data) root.right = deleteNode(root.right, key);
	    else
	    {
	        if (root.left == null){
	            Node t = root.right;
	            return t;
	        }
	        else if (root.right == null) {
	            Node t = root.left;
	            return t;
	        }
	 
	        // node with two children: Get the inorder successor (smallest in the right subtree)
	        Node temp = minValueNode(root.right);
	        // Copy the inorder successor's content to this node
	        root.data = temp.data;
	        // Delete the inorder successor
	        root.right = deleteNode(root.right, (int)temp.data);
	    }
	    return root;
	}

	final int capacity() {
		return (arr != null) ? arr.length :0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		ResizeableArray<T> result = null;
		try {
			result = (ResizeableArray<T>)super.clone();
		} catch (CloneNotSupportedException e) {
		
		}
		result.arr = (Node[]) new Object[arr.length];
		result.temp = null;
		return result;
	}
	@SuppressWarnings("unchecked")
	void clear(){
		this.arr= (Node[]) new Object[10];
		this.temp= null;
	}
}
