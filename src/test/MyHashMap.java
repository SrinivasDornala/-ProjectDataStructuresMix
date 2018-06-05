package com.api.test;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class MyHashMap extends AbstractMap<String,String>
implements Map<String,String>, Cloneable, Serializable {
	    static final int DEFAULT_INITIAL_CAPACITY = 16;
	    static final int MAXIMUM_CAPACITY = 1 << 30;
	    static final float DEFAULT_LOAD_FACTOR = 0.75f;
	    transient Entry<String,String>[] table;
	    transient int size;
	    final float loadFactor;
	    transient int modCount;
	    static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;
	    transient boolean useAltHashing;
	    transient final int hashSeed = 0;//sun.misc.Hashing.randomHashSeed(this);
	    public MyHashMap(int initialCapacity, float loadFactor) {
	        if (initialCapacity < 0)
	            throw new IllegalArgumentException("Illegal initial capacity: " +
	                                               initialCapacity);
	        if (initialCapacity > MAXIMUM_CAPACITY)
	            initialCapacity = MAXIMUM_CAPACITY;
	        if (loadFactor <= 0 || Float.isNaN(loadFactor))
	            throw new IllegalArgumentException("Illegal load factor: " +
	                                               loadFactor);

	        // Find a power of 2 >= initialCapacity
	        int capacity = 1;
	        while (capacity < initialCapacity)
	            capacity <<= 1;

	        this.loadFactor = loadFactor;
	        table = new Entry[capacity];
	        init();
	    }
	    public MyHashMap() {
	        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	        System.out.println("MyHashMap.MyHashMap()");
	    }

	    void init() {
	    	System.out.println("MyHashMap.init()");
	    }


	    final int hash(Object k) {
	        int h = 0;
	        if (useAltHashing) {
	            if (k instanceof String) {
	                return 0;
	            }
	            h = hashSeed;
	        }

	        h ^= k.hashCode();

	        // This function ensures that hashCodes that differ only by
	        // constant multiples at each bit position have a bounded
	        // number of collisions (approximately 8 at default load factor).
	        h ^= (h >>> 20) ^ (h >>> 12);
	        return h ^ (h >>> 7) ^ (h >>> 4);
	    }

	    /**
	     * Returns index for hash code h.
	     */
	    static int indexFor(int h, int length) {
	        return h & (length-1);
	    }

	    /**
	     * Returns the number of key-value mappings in this map.
	     *
	     * @return the number of key-value mappings in this map
	     */
	    public int size() {
	        return size;
	    }

	    /**
	     * Returns <tt>true</tt> if this map contains no key-value mappings.
	     *
	     * @return <tt>true</tt> if this map contains no key-value mappings
	     */
	    public boolean isEmpty() {
	        return size == 0;
	    }

	    public String get(Object key) {
	    	System.out.println("MyHashMap.get()");
	        if (key == null)
	            return getForNullKey();
	        Entry<String,String> entry = getEntry(key);

	        return null == entry ? null : entry.getValue();
	    }


	    private String getForNullKey() {
	        for (Entry<String,String> e = table[0]; e != null; e = e.next) {
	            if (e.key == null)
	                return e.value;
	        }
	        return null;
	    }

	    public boolean containsKey(Object key) {
	    	System.out.println("MyHashMap.containsKey()");
	        return getEntry(key) != null;
	    }

	    /**
	     * Returns the entry associated with the specified key in the
	     * HashMap.  Returns null if the HashMap contains no mapping
	     * for the key.
	     */
	    final Entry<String,String> getEntry(Object key) {
	    	System.out.println("MyHashMap.getEntry()");
	        int hash = (key == null) ? 0 : hash(key);
	        for (Entry<String ,String> e = table[indexFor(hash, table.length)];e != null;e = e.next) {
	            Object k;
	            if (e.hash == hash &&((k = e.key) == key || (key != null && key.equals(k))))return e;
	        }
	        return null;
	    }

	    public String put(String  key, String value) {
	    	System.out.println("MyHashMap.put()");
	        if (key == null)
	            return putForNullKey(value);
	        int hash = hash(key);
	        int i = indexFor(hash, table.length);
	        for (Entry<String ,String> e = table[i]; e != null; e = e.next) {
	            Object k;
	            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
	                String oldValue = e.value;
	                e.value = value;
	                e.recordAccess(this);
	                return oldValue;
	            }
	        }

	        modCount++;
	        addEntry(hash, key, value, i);
	        return null;
	    }

	    /**
	     * Offloaded version of put for null keys
	     */
	    private String putForNullKey(String value) {
	        for (Entry<String ,String> e = table[0]; e != null; e = e.next) {
	            if (e.key == null) {
	                String oldValue = e.value;
	                e.value = value;
	                e.recordAccess(this);
	                return oldValue;
	            }
	        }
	        modCount++;
	        addEntry(0, null, value, 0);
	        return null;
	    }


	    private void putForCreate(String key, String value) {
	        int hash = null == key ? 0 : hash(key);
	        int i = indexFor(hash, table.length);

	        for (Entry<String ,String> e = table[i]; e != null; e = e.next) {
	            Object k;
	            if (e.hash == hash &&
	                ((k = e.key) == key || (key != null && key.equals(k)))) {
	                e.value = value;
	                return;
	            }
	        }

	        createEntry(hash, key, value, i);
	    }

	    private void putAllForCreate(Map<? extends String, ? extends String> m) {
	        for (Map.Entry<? extends String, ? extends String> e : m.entrySet())
	            putForCreate(e.getKey(), e.getValue());
	    }

	    void resize(int newCapacity) {
	    	System.out.println("MyHashMap.resize()");
	        Entry[] oldTable = table;
	        int oldCapacity = oldTable.length;

	        Entry[] newTable = new Entry[newCapacity];
	        boolean oldAltHashing = useAltHashing;
	        boolean rehash = oldAltHashing ^ useAltHashing;
	        transfer(newTable, rehash);
	        table = newTable;
	    }

	    /**
	     * Transfers all entries from current table to newTable.
	     */
	    void transfer(Entry[] newTable, boolean rehash) {
	    	System.out.println("MyHashMap.transfer()");
	        int newCapacity = newTable.length;
	        for (Entry<String ,String> e : table) {
	            while(null != e) {
	            	Entry<String ,String> next = e.next;
	                if (rehash) {
	                    e.hash = null == e.key ? 0 : hash(e.key);
	                }
	                int i = indexFor(e.hash, newCapacity);
	                e.next = newTable[i];
	                newTable[i] = e;
	                e = next;
	            }
	        }
	    }

	    /**
	     * Copies all of the mappings from the specified map to this map.
	     * These mappings will replace any mappings that this map had for
	     * any of the keys currently in the specified map.
	     *
	     * @param m mappings to be stored in this map
	     * @throws NullPointerException if the specified map is null
	     */
	    public void putAll(Map<? extends String, ? extends String> m) {
	        int numKeysToBeAdded = m.size();
	        if (numKeysToBeAdded == 0)
	            return;

	        for (Map.Entry<? extends String, ? extends String> e : m.entrySet())
	            put(e.getKey(), e.getValue());
	    }

	    /**
	     * Removes the mapping for the specified key from this map if present.
	     *
	     * @param  key key whose mapping is to be removed from the map
	     * @return the previous value associated with <tt>key</tt>, or
	     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
	     *         (A <tt>null</tt> return can also indicate that the map
	     *         previously associated <tt>null</tt> with <tt>key</tt>.)
	     */
	    public String remove(Object key) {
	    	Entry<String ,String> e = removeEntryForKey(key);
	        return (e == null ? null : e.value);
	    }

	    /**
	     * Removes and returns the entry associated with the specified key
	     * in the HashMap.  Returns null if the HashMap contains no mapping
	     * for this key.
	     */
	    final Entry<String ,String> removeEntryForKey(Object key) {
	        int hash = (key == null) ? 0 : hash(key);
	        int i = indexFor(hash, table.length);
	        Entry<String ,String> prev = table[i];
	        Entry<String ,String> e = prev;

	        while (e != null) {
	        	Entry<String ,String> next = e.next;
	            Object k;
	            if (e.hash == hash &&
	                ((k = e.key) == key || (key != null && key.equals(k)))) {
	                modCount++;
	                size--;
	                if (prev == e)
	                    table[i] = next;
	                else
	                    prev.next = next;
	                e.recordRemoval(this);
	                return e;
	            }
	            prev = e;
	            e = next;
	        }

	        return e;
	    }

	    /**
	     * Special version of remove for EntrySet using {@code Map.Entry.equals()}
	     * for matching.
	     */
	    final Entry<String ,String> removeMapping(Object o) {
	        if (!(o instanceof Map.Entry))
	            return null;

	        Map.Entry<String ,String> entry = (Map.Entry<String ,String>) o;
	        Object key = entry.getKey();
	        int hash = (key == null) ? 0 : hash(key);
	        int i = indexFor(hash, table.length);
	        Entry<String ,String> prev = table[i];
	        Entry<String ,String> e = prev;

	        while (e != null) {
	        	Entry<String ,String> next = e.next;
	            if (e.hash == hash && e.equals(entry)) {
	                modCount++;
	                size--;
	                if (prev == e)
	                    table[i] = next;
	                else
	                    prev.next = next;
	                e.recordRemoval(this);
	                return e;
	            }
	            prev = e;
	            e = next;
	        }

	        return e;
	    }

	    /**
	     * Removes all of the mappings from this map.
	     * The map will be empty after this call returns.
	     */
	    public void clear() {
	    	System.out.println("MyHashMap.clear()");
	        modCount++;
	        Entry[] tab = table;
	        for (int i = 0; i < tab.length; i++)
	            tab[i] = null;
	        size = 0;
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
	    	System.out.println("MyHashMap.containsValue()");
	        if (value == null)
	            return containsNullValue();

	        Entry[] tab = table;
	        for (int i = 0; i < tab.length ; i++)
	            for (Entry e = tab[i] ; e != null ; e = e.next)
	                if (value.equals(e.value))
	                    return true;
	        return false;
	    }

	    /**
	     * Special-case code for containsValue with null argument
	     */
	    private boolean containsNullValue() {
	    	System.out.println("MyHashMap.containsNullValue()");
	        Entry[] tab = table;
	        for (int i = 0; i < tab.length ; i++)
	            for (Entry e = tab[i] ; e != null ; e = e.next)
	                if (e.value == null)
	                    return true;
	        return false;
	    }

	    /**
	     * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
	     * values themselves are not cloned.
	     *
	     * @return a shallow copy of this map
	     */
	    public Object clone() {
	        MyHashMap result = null;
	        try {
	            result = (MyHashMap)super.clone();
	        } catch (CloneNotSupportedException e) {
	            // assert false;
	        }
	        result.table = new Entry[table.length];
	        result.entrySet = null;
	        result.modCount = 0;
	        result.size = 0;
	        result.init();
	        result.putAllForCreate(this);

	        return result;
	    }

	    static class Entry<K,V> implements Map.Entry<K,V> {
	        final K key;
	        V value;
	        Entry<K,V> next;
	        int hash;

	        /**
	         * Creates new entry.
	         */
	        Entry(int h, K k, V v, Entry<K,V> n) {
	        	System.out.println("MyHashMap.Entry.Entry()");
	            value = v;
	            next = n;
	            key = k;
	            hash = h;
	        }

	        public final K getKey() {
	            return key;
	        }

	        public final V getValue() {
	            return value;
	        }

	        public final V setValue(V newValue) {
	            V oldValue = value;
	            value = newValue;
	            return oldValue;
	        }

	        public final boolean equals(Object o) {
	            if (!(o instanceof Map.Entry))
	                return false;
	            Map.Entry e = (Map.Entry)o;
	            Object k1 = getKey();
	            Object k2 = e.getKey();
	            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
	                Object v1 = getValue();
	                Object v2 = e.getValue();
	                if (v1 == v2 || (v1 != null && v1.equals(v2)))
	                    return true;
	            }
	            return false;
	        }

	        public final int hashCode() {
	            return (key==null   ? 0 : key.hashCode()) ^
	                   (value==null ? 0 : value.hashCode());
	        }

	        public final String toString() {
	            return getKey() + "=" + getValue();
	        }

	        /**
	         * This method is invoked whenever the value in an entry is
	         * overwritten by an invocation of put(k,v) for a key k that's already
	         * in the HashMap.
	         */
	        void recordAccess(MyHashMap m) {
	        }

	        /**
	         * This method is invoked whenever the entry is
	         * removed from the table.
	         */
	        void recordRemoval(MyHashMap m) {
	        }
	    }


	    void addEntry(int hash, String key, String value, int bucketIndex) {
	      System.out.println("MyHashMap.addEntry()");
	        createEntry(hash, key, value, bucketIndex);
	    }

	    void createEntry(int hash, String key, String value, int bucketIndex) {
	    	System.out.println("MyHashMap.createEntry()");
	    	Entry<String ,String> e = table[bucketIndex];
	        table[bucketIndex] = new Entry<>(hash, key, value, e);
	        size++;
	    }

	    private abstract class HashIterator<E> implements Iterator<E> {
	    	Entry<String ,String> next;        // next entry to return
	        int expectedModCount;   // For fast-fail
	        int index;              // current slot
	        Entry<String ,String> current;     // current entry

	        HashIterator() {
	            expectedModCount = modCount;
	            if (size > 0) { // advance to first entry
	                Entry[] t = table;
	                while (index < t.length && (next = t[index++]) == null)
	                    ;
	            }
	        }

	        public final boolean hasNext() {
	            return next != null;
	        }

	        final Entry<String ,String> nextEntry() {
	            if (modCount != expectedModCount)
	                throw new ConcurrentModificationException();
	            Entry<String ,String> e = next;
	            if (e == null)
	                throw new NoSuchElementException();

	            if ((next = e.next) == null) {
	                Entry[] t = table;
	                while (index < t.length && (next = t[index++]) == null)
	                    ;
	            }
	            current = e;
	            return e;
	        }

	        public void remove() {
	            if (current == null)
	                throw new IllegalStateException();
	            if (modCount != expectedModCount)
	                throw new ConcurrentModificationException();
	            Object k = current.key;
	            current = null;
	            MyHashMap.this.removeEntryForKey(k);
	            expectedModCount = modCount;
	        }
	    }

	    private final class ValueIterator extends HashIterator<String> {
	        public String next() {
	            return nextEntry().value;
	        }
	    }

	    private final class KeyIterator extends HashIterator<String> {
	        public String next() {
	            return nextEntry().getKey();
	        }
	    }

	    private final class EntryIterator extends HashIterator<Map.Entry<String ,String>> {
	        public Map.Entry<String ,String> next() {
	            return nextEntry();
	        }
	    }

	    // Subclass overrides these to alter behavior of views' iterator() method
	    Iterator<String> newKeyIterator()   {
	        return new KeyIterator();
	    }
	    Iterator<String> newValueIterator()   {
	        return new ValueIterator();
	    }
	    Iterator<Map.Entry<String ,String>> newEntryIterator()   {
	        return new EntryIterator();
	    }


	    // Views

	    private transient Set<Map.Entry<String ,String>> entrySet = null;

	    public Set<Map.Entry<String,String>> entrySet() {
	        return entrySet0();
	    }

	    private Set<Map.Entry<String,String>> entrySet0() {
	        Set<Map.Entry<String,String>> es = entrySet;
	        return es != null ? es : (entrySet = new EntrySet());
	    }

	    private final class EntrySet extends AbstractSet<Map.Entry<String,String>> {
	        public Iterator<Map.Entry<String,String>> iterator() {
	            return newEntryIterator();
	        }
	        public boolean contains(Object o) {
	            if (!(o instanceof Map.Entry))
	                return false;
	            Map.Entry<String,String> e = (Map.Entry<String,String>) o;
	            Entry<String,String> candidate = getEntry(e.getKey());
	            return candidate != null && candidate.equals(e);
	        }
	        public boolean remove(Object o) {
	            return removeMapping(o) != null;
	        }
	        public int size() {
	            return size;
	        }
	        public void clear() {
	            MyHashMap.this.clear();
	        }
	    }

	  
	    private void writeObject(java.io.ObjectOutputStream s)
	        throws IOException
	    {
	        Iterator<Map.Entry<String,String>> i =
	            (size > 0) ? entrySet0().iterator() : null;

	        // Write out the threshold, loadfactor, and any hidden stuff
	        s.defaultWriteObject();

	        // Write out number of buckets
	        s.writeInt(table.length);

	        // Write out size (number of Mappings)
	        s.writeInt(size);

	        // Write out keys and values (alternating)
	        if (size > 0) {
	            for(Map.Entry<String,String> e : entrySet0()) {
	                s.writeObject(e.getKey());
	                s.writeObject(e.getValue());
	            }
	        }
	    }

	    private static final long serialVersionUID = 362498820763181265L;

	    /**
	     * Reconstitute the {@code HashMap} instance from a stream (i.e.,
	     * deserialize it).
	     */
	    private void readObject(java.io.ObjectInputStream s)
	         throws IOException, ClassNotFoundException
	    {
	        // Read in the threshold (ignored), loadfactor, and any hidden stuff
	        s.defaultReadObject();
	        if (loadFactor <= 0 || Float.isNaN(loadFactor))
	            throw new InvalidObjectException("Illegal load factor: " +
	                                               loadFactor);

	        // Read in number of buckets and allocate the bucket array;
	        s.readInt(); // ignored

	        // Read number of mappings
	        int mappings = s.readInt();
	        if (mappings < 0)
	            throw new InvalidObjectException("Illegal mappings count: " +
	                                               mappings);

	        int initialCapacity = (int) Math.min(
	                // capacity chosen by number of mappings
	                // and desired load (if >= 0.25)
	                mappings * Math.min(1 / loadFactor, 4.0f),
	                // we have limits...
	                MyHashMap.MAXIMUM_CAPACITY);
	        int capacity = 1;
	        // find smallest power of two which holds all mappings
	        while (capacity < initialCapacity) {
	            capacity <<= 1;
	        }

	        table = new Entry[capacity];
	       

	        init();  // Give subclass a chance to do its thing.

	        // Read the keys and values, and put the mappings in the HashMap
	        for (int i=0; i<mappings; i++) {
	            String key = (String) s.readObject();
	            String value = (String) s.readObject();
	            putForCreate(key, value);
	        }
	    }

	    // These methods are used when serializing HashSets
	    int   capacity()     { return table.length; }
	    float loadFactor()   { return loadFactor;   }
	}

