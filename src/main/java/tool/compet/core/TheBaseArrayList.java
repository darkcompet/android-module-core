package tool.compet.core;

public class TheBaseArrayList {
	// Current size
	protected int size;

	public int size() {
		return this.size;
	}

	/**
	 * Clear elements by set size to 0.
	 */
	public void clear() {
		this.size = 0;
	}
}
