package tool.compet.core

/**
 * Provides conversion between all objects, for eg,. Primitives, Pixel, Dp, Sp,...
 * Functions inside this are safe and faster than Framework.
 */

fun Boolean?.parseIntDk(): Int {
	return if (this == true) 1 else 0
}

fun Boolean.parseIntDk(): Int {
	return if (this) 1 else 0
}

fun Int?.parseBooleanDk(): Boolean {
	return this != null && this != 0
}

fun String?.parseBooleanDk(): Boolean {
	return "1" == this || "true".equals(this, ignoreCase = true)
}

fun String?.parseIntDk(): Int {
	if (this == null) {
		return 0
	}

	val N = this.length
	var result = 0
	var minus = false
	var index = 0

	// Skip plus digit
	while (index < N && this[index] == '+') {
		++index
	}
	// Check minus number
	while (index < N && this[index] == '-') {
		++index
		minus = !minus
	}

	// Read continuous numder-digits
	while (index < N) {
		val ch = this[index++]
		if (ch in '0'..'9') {
			result = (result shl 3) + (result shl 1) + (ch - '0') // 10x + d
		}
		else {
			break
		}
	}
	return if (minus) -result else result
}

fun String?.parseLongDk(): Long {
	if (this == null) {
		return 0
	}

	val N = this.length
	var result: Long = 0
	var minus = false
	var index = 0

	// Skip plus digit
	while (index < N && this[index] == '+') {
		++index
	}
	// Check minus number
	while (index < N && this[index] == '-') {
		++index
		minus = !minus
	}

	// Read continuous numder-digits
	while (index < N) {
		val ch = this[index++]
		if (ch in '0'..'9') {
			result = (result shl 3) + (result shl 1) + (ch - '0') // 10x + d
		}
		else {
			break
		}
	}
	return if (minus) -result else result
}

fun String?.parseFloatDk(): Float {
	return try {
		if (this == null) 0f else java.lang.Float.parseFloat(this)
	}
	catch (ignore: Exception) {
		0f
	}
}

fun String?.parseDoubleDk(): Double {
	return try {
		if (this == null) 0.0 else java.lang.Double.parseDouble(this)
	}
	catch (ignore: Exception) {
		0.0
	}
}
