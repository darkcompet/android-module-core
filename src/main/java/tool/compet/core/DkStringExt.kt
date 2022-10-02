package tool.compet.core

fun String.trimWhiteSpaceDk() : String {
	return trim { it <= ' ' }
}

fun CharSequence.trimWhiteSpaceDk() : CharSequence {
	return trim { it <= ' ' }
}
