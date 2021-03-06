/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import java.util.Iterator;
import java.util.Locale;

import android.content.Context;

/**
 * String utilities in Java.
 */
public class DkStrings {
	public static String format(Context context, int format, Object... args) {
		return tool.compet.core.DkStrings.format(context.getString(format), args);
	}

	/**
	 * Remove from given `msg` start-leading and end-leading characters
	 * which is WHITESPACE character or character in given `delimiters`.
	 */
	public static String trimMore(String msg, char... delimiters) {
		if (msg == null || msg.length() == 0) {
			return msg;
		}

		boolean fromLeft = true;
		boolean fromRight = true;
		final boolean shouldCheckTargets = (delimiters != null);
		final int N = msg.length();
		int startIndex = 0, endIndex = N - 1;

		while (startIndex <= endIndex && (fromLeft || fromRight)) {
			// Check from left to right
			if (fromLeft) {
				char current = msg.charAt(startIndex);
				boolean stopCheck = true;

				// Check whether character insides targets
				if (shouldCheckTargets && DkArrays.inArray(current, delimiters)) {
					stopCheck = false;
					++startIndex;
				}
				// Stop checking whitespace since found this character in targets
				if (stopCheck) {
					fromLeft = false;
				}
				// Check whether the character is whitespace
				else if (Character.isWhitespace((int) current)) {
					++startIndex;
				}
			}

			// Check from right to left
			if (fromRight) {
				char current = msg.charAt(endIndex);
				boolean stopCheck = true;

				// Check whether the character insides targets
				if (shouldCheckTargets && DkArrays.inArray(current, delimiters)) {
					stopCheck = false;
					--endIndex;
				}
				// Stop checking whitespace since found this character in targets
				if (stopCheck) {
					fromRight = false;
				}
				// Check whether the character is whitespace
				else if (Character.isWhitespace((int) current)) {
					--endIndex;
				}
			}
		}

		return (endIndex < startIndex) ? "" : msg.substring(startIndex, endIndex + 1);
	}

	/**
	 * Remove from given `msg` start-leading and end-leading characters
	 * which is character in given `delimiters`.
	 */
	public static String trimExact(String msg, char... delimiters) {
		if (msg == null || msg.length() == 0 || delimiters == null || delimiters.length == 0) {
			return msg;
		}

		boolean fromLeft = true;
		boolean fromRight = true;
		final int N = msg.length();
		int startIndex = 0, endIndex = N - 1;

		while (startIndex <= endIndex && (fromLeft || fromRight)) {
			// check from left to right
			if (fromLeft) {
				char current = msg.charAt(startIndex);
				boolean stopCheck = true;
				// check whether current insides targets
				if (DkArrays.inArray(current, delimiters)) {
					stopCheck = false;
					++startIndex;
				}
				if (stopCheck) {
					fromLeft = false;
				}
			}
			// check from right to left
			if (fromRight) {
				char current = msg.charAt(endIndex);
				boolean stopCheck = true;
				// check whether current insides targets
				if (DkArrays.inArray(current, delimiters)) {
					stopCheck = false;
					--endIndex;
				}
				if (stopCheck) {
					fromRight = false;
				}
			}
		}

		return (endIndex < startIndex) ? "" : msg.substring(startIndex, endIndex + 1);
	}

	public static boolean isEquals(CharSequence a, CharSequence b) {
		return a == b || (a != null && a.equals(b));
	}

	public static String join(char delimiter, String... items) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : items) {
			if (first) {
				first = false;
			}
			else {
				sb.append(delimiter);
			}
			sb.append(item);
		}
		return sb.toString();
	}

	public static String join(char delimiter, Iterable<String> items) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = items.iterator();

		if (it.hasNext()) {
			sb.append(it.next());

			while (it.hasNext()) {
				sb.append(delimiter);
				sb.append(it.next());
			}
		}
		return sb.toString();
	}

	public static String join(CharSequence delimiter, Iterable<String> items) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> it = items.iterator();

		if (it.hasNext()) {
			sb.append(it.next());

			while (it.hasNext()) {
				sb.append(delimiter);
				sb.append(it.next());
			}
		}
		return sb.toString();
	}

	public static String join(CharSequence delimiter, String... items) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (String item : items) {
			if (first) {
				first = false;
			}
			else {
				sb.append(delimiter);
			}
			sb.append(item);
		}

		return sb.toString();
	}

	public static String format(String format, Object... args) {
		return format == null || (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
	}
}
