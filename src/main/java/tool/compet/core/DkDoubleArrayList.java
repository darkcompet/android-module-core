/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

/**
 * This is performance-better version of {@link java.util.ArrayList} for primitive type.
 *
 * Because this aims to fast performance, range for index will NOT be checked.
 * When handle with `index`, take care of passing valid range for index.
 */
public class DkDoubleArrayList extends TheBaseArrayList {
	// Internal array (current snapshot of elements)
	private double[] arr;

	public DkDoubleArrayList() {
		this(10);
	}

	public DkDoubleArrayList(int capacity) {
		if (capacity <= 0) {
			capacity = 10;
		}
		this.arr = new double[capacity];
	}

	/**
	 * @return Left-most index of element which equals to given `element`. If not found return -1.
	 */
	public int indexOf(double element) {
		double[] arr = this.arr;
		for (int index = 0, N = this.size; index < N; ++index) {
			if (arr[index] == element) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * @return Right-most index of element which equals to given `element`. If not found return -1.
	 */
	public int lastIndexOf(double element) {
		double[] arr = this.arr;
		for (int index = this.size - 1; index >= 0; --index) {
			if (arr[index] == element) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Add (append) new `element` to last of array.
	 */
	public void add(double element) {
		int newSize = this.size + 1;
		if (newSize >= this.arr.length) {
			growCapacity(newSize);
		}
		this.arr[this.size++] = element;
	}

	/**
	 * Add (insert at) new `element` at given `index` of array list.
	 *
	 * @param index Must in range [0, size].
	 */
	public void add(int index, double element) {
		// Grow capacity if need
		final int size = this.size;
		if (size + 1 >= this.arr.length) {
			growCapacity(size + 1);
		}
		// Shift right elements at `index`
		if (index < size) {
			System.arraycopy(this.arr, index, this.arr, index + 1, size - index);
		}
		this.arr[index] = element;
		this.size++;
	}

	/**
	 * Add new element if not exists.
	 */
	public boolean addIfAbsence(double element) {
		if (indexOf(element) < 0) {
			add(element);
			return true;
		}
		return false;
	}

	public void addAll(double[] elements) {
		addRange(this.size, elements, 0, elements.length);
	}

	public void addAll(int index, double[] elements) {
		addRange(index, elements, 0, elements.length);
	}

	public void addRange(double[] elements, int startIndex, int endIndex) {
		addRange(this.size, elements, startIndex, endIndex);
	}

	/**
	 * Add a range of `elements` at given `index`.
	 * Caller must pass valid `index` in range [0, size].
	 *
	 * @param index Insert position. Must in range [0, size).
	 * @param elements Data to copy.
	 * @param startIndex Start-index of copy-range inclusive.
	 * @param endIndex End-index of copy-range exclusive.
	 */
	public void addRange(int index, double[] elements, int startIndex, int endIndex) {
		final int oldSize = this.size;
		final int addMore = endIndex - startIndex;
		final int newSize = oldSize + addMore;

		// Grow capacity if need
		if (newSize >= this.arr.length) {
			growCapacity(newSize);
		}

		// Move elements in [index, oldSize) to `addMore` steps
		if (oldSize > index) {
			System.arraycopy(this.arr, index, this.arr, index + addMore, oldSize - index);
		}

		// Insert `addMore` elements to [index, index + addMore)
		System.arraycopy(elements, startIndex, this.arr, index, addMore);

		this.size = newSize;
	}

	/**
	 * Remove left-most element which equals to given `element` without reallocate internal array.
	 *
	 * Note: this will copy last element into element at given `index`,
	 * so use it if you do NOT care about order of elements after removed.
	 *
	 * @return TRUE if succeed (found element). Otherwise FALSE.
	 */
	public boolean fastRemoveElement(double element) {
		int index = indexOf(element);
		if (index >= 0) {
			fastRemove(index);
			return true;
		}
		return false;
	}

	/**
	 * Remove the element at given `index` without reallocate internal array.
	 *
	 * Note: this will copy last element into element at given `index`,
	 * so use it if you do NOT care about order of elements after removed.
	 *
	 * @param index Must in range [0, size).
	 */
	public void fastRemove(int index) {
		int lastIndex = this.size - 1;
		if (index < 0 || index > lastIndex) {
			throw new RuntimeException("Invalid index: " + index);
		}
		if (index < lastIndex) {
			this.arr[index] = this.arr[lastIndex];
		}
		this.size--;
	}

	/**
	 * Remove left-most element which equals to given `element`.
	 *
	 * @return TRUE if succeed (found element).
	 */
	public boolean removeElement(double element) {
		int index = indexOf(element);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Remove element at given `index`.
	 *
	 * @param index Must in range [0, size).
	 */
	public void remove(int index) {
		System.arraycopy(this.arr, index + 1, this.arr, index, this.size - 1 - index);
		this.size--;
	}

	/**
	 * Get value of element at given `index`.
	 *
	 * @param index Must in range [0, size).
	 */
	public double get(int index) {
		return this.arr[index];
	}

	/**
	 * Set value to element at given `index`.
	 *
	 * @param index Must in range [0, size).
	 */
	public void set(int index, double element) {
		this.arr[index] = element;
	}

	/**
	 * Check existence of element which equals to given `element`.
	 */
	public boolean contains(double element) {
		return indexOf(element) >= 0;
	}

	/**
	 * Grows up if current internal array length is smaller than given `minCapacity`.
	 */
	public void ensureCapacity(int minCapacity) {
		if (this.arr.length <= minCapacity) {
			growCapacity(minCapacity);
		}
	}

	/**
	 * Get current snapshot of internal array. Because capacity of internal array maybe bigger than actual size of it,
	 * so caller take care of checking iteration-index with `size()` of result-array when take an action.
	 */
	public double[] getCurrentArray() {
		return this.arr;
	}

	/**
	 * @return Clone new array from internal array in range [0, size).
	 */
	public double[] toArray() {
		double[] result = new double[this.size];
		System.arraycopy(this.arr, 0, result, 0, this.size);
		return result;
	}

	// Make internal array bigger enough to make its capacity greater than given `minCapacity`.
	private void growCapacity(int minCapacity) {
		int newCapacity = MyArrayHelper.calcNextCapacity(this.arr.length, minCapacity, Integer.MAX_VALUE - 8);

		// Make new array and then copy from old array.
		double[] newArr = new double[newCapacity];
		System.arraycopy(this.arr, 0, newArr, 0, this.size);

		this.arr = newArr;
	}
}