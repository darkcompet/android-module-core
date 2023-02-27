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
	return try {
		if (this == null) 0 else java.lang.Integer.parseInt(this)
	}
	catch (ignore: Exception) {
		0
	}
}

fun String?.parseLongDk(): Long {
	return try {
		if (this == null) 0L else java.lang.Long.parseLong(this)
	}
	catch (ignore: Exception) {
		0L
	}
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
