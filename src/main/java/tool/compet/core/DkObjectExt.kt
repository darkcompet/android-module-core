package tool.compet.core

import java.lang.reflect.Array

/**
 * @return TRUE if given object is null, empty string, 0-size...
 */
fun Any?.isEmptyDk() : Boolean {
	return if (this == null) {
		true
	}
	else if (this is CharSequence) {
		this.length == 0
	}
	else if (this is Collection<*>) {
		this.size == 0
	}
	else if (this is Map<*, *>) {
		this.size == 0
	}
	else if (this.javaClass.isArray) {
		Array.getLength(this) == 0
	}
	else false
}
