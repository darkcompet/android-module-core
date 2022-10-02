/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.annotation.NonNull;

/**
 * This maps primitive `int` vs `Object`.
 */
@SuppressWarnings("unchecked")
public class DkIntObjectArrayMap<V> extends TheBaseSparseArray {
	private V[] values;

	public DkIntObjectArrayMap() {
		this(10);
	}

	public DkIntObjectArrayMap(int capacity) {
		if (capacity <= 0) {
			this.keys = DkEmptyArray.INT;
			this.values = (V[]) DkEmptyArray.OBJECT;
		}
		else {
			this.keys = new int[capacity];
			this.values = (V[]) new Object[capacity];
		}
	}

	/**
	 * @return Value mapped from the specified key if found. Otherwise return default value.
	 */
	public V get(int key, V defaultValue) {
		int index = MyArrayHelper.binarySeearch(this.keys, this.size, key);
		return index >= 0 ? this.values[index] : defaultValue;
	}

	/**
	 * Removes the mapping from given key if exists.
	 */
	public void remove(int key) {
		int index = MyArrayHelper.binarySeearch(this.keys, this.size, key);
		if (index >= 0) {
			removeAt(index);
		}
	}

	/**
	 * Removes the mapping at given index.
	 *
	 * @param index Must be in range [0, size).
	 */
	public void removeAt(int index) {
		final int length = this.size - (index + 1);
		System.arraycopy(this.keys, index + 1, this.keys, index, length);
		System.arraycopy(this.values, index + 1, this.values, index, length);
		this.size--;
	}

	/**
	 * Adds a mapping from the specified key to the specified value,
	 * replacing the previous mapping from the specified key if there
	 * was one.
	 */
	public void put(int key, V value) {
		final int size = this.size;
		int index = MyArrayHelper.binarySeearch(this.keys, size, key);

		if (index >= 0) {
			this.values[index] = value;
		}
		else {
			index = ~index;

			this.keys = MyArrayHelper.insert(this.keys, size, index, key);
			this.values = MyArrayHelper.insert(this.values, size, index, value);
			this.size++;
		}
	}

	/**
	 * Get value at given index.
	 *
	 * @param index Must be in range [0, size).
	 */
	public V valueAt(int index) {
		return this.values[index];
	}

	/**
	 * Set value at given index.
	 *
	 * @param index Must be in range [0, size).
	 */
	public void setValueAt(int index, V value) {
		this.values[index] = value;
	}

	/**
	 * Linear find first index for given value from left to right (leftmost).
	 *
	 * @return Index of given value if found. Otherwise return -1.
	 */
	public int indexOfValue(V value) {
		final int size = this.size;
		int index = -1;
		for (V element : this.values) {
			if (++index >= size) return -1;
			if (element == value) return index;
		}
		return -1;
	}

	/**
	 * Puts a key/value pair into the array, optimizing for the case where
	 * the key is greater than all existing keys in the array.
	 */
	public void append(int key, V value) {
		final int size = this.size;
		// Put into array
		if (size > 0 && key <= this.keys[size - 1]) {
			put(key, value);
			return;
		}
		// Just append to last
		this.keys = MyArrayHelper.append(this.keys, size, key);
		this.values = MyArrayHelper.append(this.values, size, value);
		this.size++;
	}

	@NonNull
	@Override
	public DkIntObjectArrayMap<V> clone() {
		DkIntObjectArrayMap<V> clone;
		try {
			clone = (DkIntObjectArrayMap<V>) super.clone();
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
	@NonNull
	@Override
	public String toString() {
		if (size() <= 0) {
			return "{}";
		}
		final int size = this.size;
		final StringBuilder buffer = new StringBuilder(size * 28);

		buffer.append('{');
		for (int index = 0; index < size; ++index) {
			if (index > 0) {
				buffer.append(", ");
			}
			buffer.append(keyAt(index)).append('=').append(valueAt(index));
		}
		buffer.append('}');

		return buffer.toString();
	}
}
