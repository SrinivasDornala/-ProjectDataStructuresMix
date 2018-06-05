package com.api.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyLinkedHashSet extends MyHashSet
implements Set, Cloneable, java.io.Serializable {

private static final long serialVersionUID = -2851667679971038690L;

/**
 * Constructs a new, empty linked hash set with the specified initial
 * capacity and load factor.
 *
 * @param      initialCapacity the initial capacity of the linked hash set
 * @param      loadFactor      the load factor of the linked hash set
 * @throws     IllegalArgumentException  if the initial capacity is less
 *               than zero, or if the load factor is nonpositive
 */
public MyLinkedHashSet(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor, true);
    System.out.println("MyLinkedHashSet.MyLinkedHashSet()");
}

/**
 * Constructs a new, empty linked hash set with the specified initial
 * capacity and the default load factor (0.75).
 *
 * @param   initialCapacity   the initial capacity of the LinkedHashSet
 * @throws  IllegalArgumentException if the initial capacity is less
 *              than zero
 */
public MyLinkedHashSet(int initialCapacity) {
    super(initialCapacity, .75f, true);
    System.out.println("MyLinkedHashSet.enclosing_method()");
}

/**
 * Constructs a new, empty linked hash set with the default initial
 * capacity (16) and load factor (0.75).
 */
public MyLinkedHashSet() {
    super(16, .75f, true);
    System.out.println("MyLinkedHashSet.MyLinkedHashSet()");
}

/**
 * Constructs a new linked hash set with the same elements as the
 * specified collection.  The linked hash set is created with an initial
 * capacity sufficient to hold the elements in the specified collection
 * and the default load factor (0.75).
 *
 * @param c  the collection whose elements are to be placed into
 *           this set
 * @throws NullPointerException if the specified collection is null
 */
public MyLinkedHashSet(Collection c) {
    super(Math.max(2*c.size(), 11), .75f, true);
    addAll(c);
}
}
