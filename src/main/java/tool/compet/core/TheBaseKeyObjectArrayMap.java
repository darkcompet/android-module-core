package tool.compet.core;

public class TheBaseKeyObjectArrayMap<K> {
	protected int[] hashes;
	protected K[] keys;
	protected int size;

	/**
	 * Check given key is found or not.
	 */
	public boolean containsKey(K key) {
		return MyArrayHelper.binarySeearch(this.hashes, this.size, key.hashCode()) >= 0;
	}

	/**
	 * @param index Must in range [0, size).
	 */
	public K keyAt(int index) {
		return this.keys[index];
	}

	/**
	 * @return Current size.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Just reset size to 0.
	 */
	public void clear() {
		this.size = 0;
	}
}
