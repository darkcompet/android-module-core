package tool.compet.core

class DkBitwise {
	companion object {
		/**
		 * Check whether bit at given index is 1.
		 */
		fun hasBitAt(value: Int, index: Int): Boolean {
			return ((value shr index) and 1) == 1
		}
	}
}