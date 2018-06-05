package com.api.test;

import java.util.Map;

public class MapEntry<K,V> implements Map.Entry<K,V> {
    final K key;
    V value;
    MapEntry<K,V> next;
    int hash;

    MapEntry(int h, K k, V v, MapEntry<K,V> n) {
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

    void recordAccess(MyHashMap m) {
    }

    void recordRemoval(MyHashMap m) {
    }
}