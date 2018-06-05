package com.api.test;

public  class LinkedEntry<K,V> extends MapEntry<K,V>{
    // These fields comprise the doubly linked list used for iteration.
	LinkedEntry<K,V> before, after;

	LinkedEntry(int hash, K key, V value, LinkedEntry<K,V> next) {
        super(hash, key, value, next);
        System.out.println("MyLinkedHashMap.Entry.Entry()");
    }

    private void remove() {
    	System.out.println("MyLinkedHashMap.Entry.remove()");
        before.after = after;
        after.before = before;
    }

    private void addBefore(LinkedEntry<K,V> existingEntry) {
    	System.out.println("MyLinkedHashMap.Entry.addBefore()");
        after  = existingEntry;
        before = existingEntry.before;
        before.after = this;
        after.before = this;
    }

    /*void recordAccess(MyHashMap m) {
        MyLinkedHashMap lm = (MyLinkedHashMap)m;
        if (lm.accessOrder) {
            lm.modCount++;
            remove();
            addBefore((LinkedEntry) lm.header);
        }
    }*/

    void recordRemoval(MyHashMap m) {
        remove();
    }
}
