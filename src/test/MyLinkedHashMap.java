package com.api.test;

import java.io.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
public class MyLinkedHashMap
    extends MyHashMap
    implements Map<String,String>
{

    private static final long serialVersionUID = 3801124242820219131L;

    /**
     * The head of the doubly linked list.
     */
    transient Entry<String,String> header;

    /**
     * The iteration ordering method for this linked hash map: <tt>true</tt>
     * for access-order, <tt>false</tt> for insertion-order.
     *
     * @serial
     */
    final boolean accessOrder;

    /**
     * Constructs an empty insertion-ordered <tt>LinkedHashMap</tt> instance
     * with the specified initial capacity and load factor.
     *
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public MyLinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
    }


    public MyLinkedHashMap() {
        super();
        System.out.println("MyLinkedHashMap.MyLinkedHashMap()");
        accessOrder = false;
    }

    @Override
    void init() {
    	System.out.println("MyLinkedHashMap.init()");
        header = new Entry<>(-1, null, null, null);
        header.before = header.after = header;
    }

    /**
     * Transfers all entries to new table array.  This method is called
     * by superclass resize.  It is overridden for performance, as it is
     * faster to iterate using our linked list.
     */
    @Override
    void transfer(MyHashMap.Entry[] newTable, boolean rehash) {
    	System.out.println("MyLinkedHashMap.transfer()");
        int newCapacity = newTable.length;
        for (Entry<String,String> e = header.after; e != header; e = e.after) {
            if (rehash)
                e.hash = (e.key == null) ? 0 : hash(e.key);
            int index = indexFor(e.hash, newCapacity);
            e.next = newTable[index];
            newTable[index] = e;
        }
    }


    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value
     */
    public boolean containsValue(Object value) {
    	System.out.println("MyLinkedHashMap.containsValue()");
        // Overridden to take advantage of faster iterator
        if (value==null) {
            for (Entry e = header.after; e != header; e = e.after)
                if (e.value==null)
                    return true;
        } else {
            for (Entry e = header.after; e != header; e = e.after)
                if (value.equals(e.value))
                    return true;
        }
        return false;
    }

    public String get(Object key) {
    	System.out.println("MyLinkedHashMap.get()");
        Entry<String,String> e = (Entry<String,String>)getEntry(key);
        if (e == null)
            return null;
        e.recordAccess(this);
        return e.value;
    }

    /**
     * Removes all of the mappings from this map.
     * The map will be empty after this call returns.
     */
    public void clear() {
        super.clear();
        System.out.println("MyLinkedHashMap.clear()");
        header.before = header.after = header;
    }

    /**
     * LinkedHashMap entry.
     */
    private static class Entry<K,V> extends MyHashMap.Entry<K,V> {
        // These fields comprise the doubly linked list used for iteration.
        Entry<K,V> before, after;

        Entry(int hash, K key, V value, MyHashMap.Entry<K,V> next) {
            super(hash, key, value, next);
            System.out.println("MyLinkedHashMap.Entry.Entry()");
        }

        /**
         * Removes this entry from the linked list.
         */
        private void remove() {
        	System.out.println("MyLinkedHashMap.Entry.remove()");
            before.after = after;
            after.before = before;
        }

        /**
         * Inserts this entry before the specified existing entry in the list.
         */
        private void addBefore(Entry<K,V> existingEntry) {
        	System.out.println("MyLinkedHashMap.Entry.addBefore()");
            after  = existingEntry;
            before = existingEntry.before;
            before.after = this;
            after.before = this;
        }

        /**
         * This method is invoked by the superclass whenever the value
         * of a pre-existing entry is read by Map.get or modified by Map.set.
         * If the enclosing Map is access-ordered, it moves the entry
         * to the end of the list; otherwise, it does nothing.
         */
        void recordAccess(MyHashMap m) {
            MyLinkedHashMap lm = (MyLinkedHashMap)m;
            if (lm.accessOrder) {
                lm.modCount++;
                remove();
                addBefore((Entry<K, V>) lm.header);
            }
        }

        void recordRemoval(MyHashMap m) {
            remove();
        }
    }

    private abstract class LinkedHashIterator<T> implements Iterator<T> {
        Entry<String,String> nextEntry    = header.after;
        Entry<String,String> lastReturned = null;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;

        public boolean hasNext() {
            return nextEntry != header;
        }

        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();

            MyLinkedHashMap.this.remove(lastReturned.key);
            lastReturned = null;
            expectedModCount = modCount;
        }

        Entry<String,String> nextEntry() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (nextEntry == header)
                throw new NoSuchElementException();

            Entry<String,String> e = lastReturned = nextEntry;
            nextEntry = e.after;
            return e;
        }
    }

    private class KeyIterator extends LinkedHashIterator<String> {
        public String next() { return nextEntry().getKey(); }
    }

    private class ValueIterator extends LinkedHashIterator<String> {
        public String next() { return nextEntry().value; }
    }

    private class EntryIterator extends LinkedHashIterator<Map.Entry<String,String>> {
        public Map.Entry<String,String> next() { return nextEntry(); }
    }

    // These Overrides alter the behavior of superclass view iterator() methods
    Iterator<String> newKeyIterator()   { return new KeyIterator();   }
    Iterator<String> newValueIterator() { return new ValueIterator(); }
    Iterator<Map.Entry<String,String>> newEntryIterator() { return new EntryIterator(); }

    /**
     * This override alters behavior of superclass put method. It causes newly
     * allocated entry to get inserted at the end of the linked list and
     * removes the eldest entry if appropriate.
     */
    void addEntry(int hash, String key, String value, int bucketIndex) {
        super.addEntry(hash, key, value, bucketIndex);
        System.out.println("MyLinkedHashMap.addEntry()");
        // Remove eldest entry if instructed
        Entry<String,String> eldest = header.after;
        if (removeEldestEntry(eldest)) {
            removeEntryForKey(eldest.key);
        }
    }

    /**
     * This override differs from addEntry in that it doesn't resize the
     * table or remove the eldest entry.
     */
    void createEntry(int hash, String key, String value, int bucketIndex) {
    	System.out.println("MyLinkedHashMap.createEntry()");
        MyHashMap.Entry<String,String> old = table[bucketIndex];
        Entry<String,String> e = new Entry<>(hash, key, value, old);
        table[bucketIndex] = e;
        e.addBefore(header);
        size++;
    }

   
    protected boolean removeEldestEntry(Map.Entry<String,String> eldest) {
        return false;
    }
}
