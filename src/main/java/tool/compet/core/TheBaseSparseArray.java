package tool.compet.core;

import java.util.Arrays;

/**
 * This aims to reduce memory compare with HashMap.
 * But since array-reallocation when insert/remove, this is slower than HashMap in time-complexity.
 *
 * If you don't handle with container-feature as iterator, we recommend use this instead of HashMap
 * when data size is small (under 100).
 *
 * Note: when change something for subclass, should start from `DkIntDoubleArrayMap` for easier type-replacement.
 *
 * Refer: {@link android.util.SparseArray}, {@link android.util.SparseIntArray}, {@link android.util.SparseLongArray}...
 */
public class TheBaseSparseArray implements Cloneable {
	protected int[] keys; // always sorted as ascending
	protected int size; // current size

	/**
	 * @return TRUE if given key was found. Otherwise FALSE.
	 */
	public boolean containsKey(int key) {
		return MyArrayHelper.binarySeearch(this.keys, this.size, key) >= 0;
	}

	/**
	 * @param index Must be in range [0, size).
	 */
	public int keyAt(int index) {
		return this.keys[index];
	}

	/**
	 * Returns the index for which {@link #keyAt} would return the
	 * specified key, or a negative number if the specified
	 * key is not mapped.
	 */
	public int indexOfKey(int key) {
		return MyArrayHelper.binarySeearch(this.keys, this.size, key);
	}

	/**
	 * @return New copy of current keys.
	 */
	public int[] copyKeys() {
		if (size() == 0) {
			return null;
		}
		return Arrays.copyOf(this.keys, this.size);
	}

	/**
	 * Returns the number of key-value mappings that this SparseIntArray
	 * currently stores.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Removes all key-value mappings from this SparseIntArray.
	 */
	public void clear() {
		this.size = 0;
	}
}
