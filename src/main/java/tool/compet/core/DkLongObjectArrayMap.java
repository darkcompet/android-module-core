/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.annotation.NonNull;

import java.util.Arrays;

/**
 * This maps long vs object.
 * We make this for Java (from Android).
 *
 * O: value type.
 */
@SuppressWarnings("unchecked")
public class DkLongObjectArrayMap<O> {
	protected long[] keys;
	protected Object[] values;
	protected int size;

	public DkLongObjectArrayMap() {
		this(10);
	}

	/**
	 * Creates a new SparseIntArray containing no mappings that will not
	 * require any additional memory allocation to store the specified
	 * number of mappings.  If you supply an initial capacity of 0, the
	 * sparse array will be initialized with a light-weight representation
	 * not requiring any additional array allocations.
	 */
	public DkLongObjectArrayMap(int initialCapacity) {
		if (initialCapacity <= 0) {
			keys = DkEmptyArray.LONG;
			values = DkEmptyArray.OBJECT;
		}
		else {
			keys = new long[initialCapacity];
			values = new Object[keys.length];
		}
		size = 0;
	}

	/**
	 * Gets the long mapped from the specified key, or `null` if no such mapping has been made.
	 */
	public O get(long key) {
		return get(key, null);
	}

	/**
	 * Gets the long mapped from the specified key, or the specified value
	 * if no such mapping has been made.
	 */
	public O get(long key, O valueIfKeyNotFound) {
		int index = MyArrayHelper.binarySeearch(keys, size, key);
		return index < 0 ? valueIfKeyNotFound : (O) values[index];
	}

	/**
	 * Removes the mapping from the specified key if there was any.
	 */
	public void remove(long key) {
		int index = MyArrayHelper.binarySeearch(keys, size, key);
		if (index >= 0) {
			removeAt(index);
		}
	}

	/**
	 * Removes the mapping at the given index (NOT key).
	 */
	public void removeAt(int index) {
		System.arraycopy(keys, index + 1, keys, index, size - (index + 1));
		System.arraycopy(values, index + 1, values, index, size - (index + 1));
		size--;
	}

	/**
	 * Adds a mapping from the specified key to the specified value,
	 * replacing the previous mapping from the specified key if there
	 * was one.
	 */
	public void put(long key, O value) {
		int index = MyArrayHelper.binarySeearch(keys, size, key);

		if (index >= 0) {
			values[index] = value;
		}
		else {
			index = ~index;

			keys = MyArrayHelper.insert(keys, size, index, key);
			values = MyArrayHelper.insert(values, size, index, value);
			size++;
		}
	}

	/**
	 * Returns the number of key-value mappings that this SparseIntArray
	 * currently stores.
	 */
	public long size() {
		return size;
	}

	/**
	 * Gets the long mapped from the specified key, or `null` if no such mapping has been made.
	 */
	public boolean contains(long key) {
		long index = MyArrayHelper.binarySeearch(keys, size, key);
		return index >= 0;
	}

	/**
	 * Given an index in the range <code>0...size()-1</code>, returns
	 * the key from the <code>index</code>th key-value mapping that this
	 * SparseIntArray stores.
	 *
	 * <p>The keys corresponding to indices in ascending order are guaranteed to
	 * be in ascending order, e.g., <code>keyAt(0)</code> will return the
	 * smallest key and <code>keyAt(size()-1)</code> will return the largest
	 * key.</p>
	 */
	public long keyAt(int index) {
		return keys[index];
	}

	/**
	 * Given an index in the range <code>0...size()-1</code>, returns
	 * the value from the <code>index</code>th key-value mapping that this
	 * SparseIntArray stores.
	 *
	 * <p>The values corresponding to indices in ascending order are guaranteed
	 * to be associated with keys in ascending order, e.g.,
	 * <code>valueAt(0)</code> will return the value associated with the
	 * smallest key and <code>valueAt(size()-1)</code> will return the value
	 * associated with the largest key.</p>
	 */
	public O valueAt(int index) {
		return (O) values[index];
	}

	/**
	 * Directly set the value at a particular index.
	 */
	public void setValueAt(int index, O value) {
		values[index] = value;
	}

	/**
	 * Returns the index for which {@link #keyAt} would return the
	 * specified key, or a negative number if the specified
	 * key is not mapped.
	 */
	public long indexOfKey(long key) {
		return MyArrayHelper.binarySeearch(keys, size, key);
	}

	/**
	 * Returns an index for which {@link #valueAt} would return the
	 * specified key, or a negative number if no keys map to the
	 * specified value.
	 * Beware that this is a linear search, unlike lookups by key,
	 * and that multiple keys can map to the same value and this will
	 * find only one of them.
	 */
	public long indexOfValue(O value) {
		for (int index = 0; index < size; index++) {
			if (values[index] == value)
				return index;
		}
		return -1;
	}

	/**
	 * Removes all key-value mappings from this SparseIntArray.
	 */
	public void clear() {
		size = 0;
	}

	/**
	 * Puts a key/value pair longo the array, optimizing for the case where
	 * the key is greater than all existing keys in the array.
	 */
	public void append(long key, O value) {
		if (size != 0 && key <= keys[size - 1]) {
			put(key, value);
			return;
		}
		keys = MyArrayHelper.append(keys, size, key);
		values = MyArrayHelper.append(values, size, value);
		size++;
	}

	/**
	 * @return New copy of current keys.
	 */
	public long[] copyKeys() {
		if (size() == 0) {
			return null;
		}
		return Arrays.copyOf(keys, size);
	}

	@NonNull
	@Override
	public DkLongObjectArrayMap<O> clone() {
		DkLongObjectArrayMap<O> clone;
		try {
			clone = (DkLongObjectArrayMap<O>) super.clone();
			clone.keys = keys.clone();
			clone.values = values.clone();
		}
		catch (CloneNotSupportedException ignore) {
			throw new RuntimeException("Not support");
		}
		return clone;
	}

	/**
	 * This implementation composes a string by iterating over its mappings.
	 */
	@Override
	public String toString() {
		if (size() <= 0) {
			return "{}";
		}
		StringBuilder buffer = new StringBuilder(size * 28);
		buffer.append('{');
		for (int index = 0; index < size; index++) {
			if (index > 0) {
				buffer.append(", ");
			}
			long key = keyAt(index);
			buffer.append(key);
			buffer.append('=');
			O value = valueAt(index);
			buffer.append(value);
		}
		buffer.append('}');
		return buffer.toString();
	}
}
