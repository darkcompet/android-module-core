package tool.compet.core;

@SuppressWarnings("unchecked")
public class DkObjectDoubleArrayMap<K> extends TheBaseKeyObjectArrayMap<K> {
	protected double[] values;

	public DkObjectDoubleArrayMap() {
		this(10);
	}

	public DkObjectDoubleArrayMap(int capacity) {
		if (capacity <= 0) {
			free();
		}
		else {
			this.hashes = new int[capacity];
			this.keys = (K[]) new Object[capacity];
			this.values = new double[capacity];
			this.size = capacity;
		}
	}

	/**
	 * Set key-value to this map.
	 * This will replace key&value if given key exists.
	 */
	public void put(K key, double value) {
		final int size = this.size;
		final int hash = key.hashCode();
		final int index = MyArrayHelper.binarySeearch(this.hashes, size, hash);

		// Found key -> Update
		if (index >= 0) {
			this.hashes[index] = hash;
			this.keys[index] = key;
			this.values[index] = value;
			return;
		}

		// Insert new pair
		final int insertIndex = ~index;
		this.hashes = MyArrayHelper.insert(this.hashes, size, insertIndex, hash);
		this.keys = MyArrayHelper.insert(this.keys, size, insertIndex, key);
		this.values = MyArrayHelper.insert(this.values, size, insertIndex, value);
		this.size++;
	}

	/**
	 * @return Value at given key or default value if not found.
	 */
	public double get(K key, double defaultValue) {
		final int index = MyArrayHelper.binarySeearch(this.hashes, this.size, key.hashCode());

		return index >= 0 ? this.values[index] : defaultValue;
	}

	/**
	 * @param index Must in range [0, size).
	 */
	public double valueAt(int index) {
		return this.values[index];
	}

	/**
	 * Reset to initial state.
	 * Use this also free arrays to reduce memory.
	 */
	public void free() {
		this.hashes = DkEmptyArray.INT;
		this.keys = (K[]) DkEmptyArray.OBJECT;
		this.values = DkEmptyArray.DOUBLE;
		this.size = 0;
	}
}
