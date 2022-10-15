package tool.compet.core

class DkBitwise {
	companion object {
		/**
		 * Check whether bit at given index is 1.
		 *
		 * @param index Start from right (end) to left (start), from 0-index.
		 */
		fun hasBitAt(value: Int, index: Int): Boolean {
			return ((value shr index) and 1) == 1
		}
	}
}