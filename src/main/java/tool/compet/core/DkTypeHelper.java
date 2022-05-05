/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.collection.ArrayMap;

/**
 * Classify (check) type of a class.
 * For eg,.
 * - `int.class` will result real-type as TYPE_INTEGER_PREMITIVE, and type as TYPE_INTEGER,
 * - `Integer.class` will result real-type as TYPE_INTEGER_OBJECT, and type as TYPE_INTEGER.
 */
public class DkTypeHelper {
	public static final int TYPE_BOOLEAN_MASKED = 0x1;
	public static final int TYPE_BOOLEAN_OBJECT = 0x11;
	public static final int TYPE_BOOLEAN_PREMITIVE = 0x12;

	public static final int TYPE_SHORT_MASKED = 0x2;
	public static final int TYPE_SHORT_OBJECT = 0x21;
	public static final int TYPE_SHORT_PREMITIVE = 0x22;

	public static final int TYPE_INTEGER_MASKED = 0x3;
	public static final int TYPE_INTEGER_OBJECT = 0x31;
	public static final int TYPE_INTEGER_PREMITIVE = 0x32;

	public static final int TYPE_LONG_MASKED = 0x4;
	public static final int TYPE_LONG_OBJECT = 0x41;
	public static final int TYPE_LONG_PREMITIVE = 0x42;

	public static final int TYPE_FLOAT_MASKED = 0x5;
	public static final int TYPE_FLOAT_OBJECT = 0x51;
	public static final int TYPE_FLOAT_PREMITIVE = 0x52;

	public static final int TYPE_DOUBLE_MASKED = 0x6;
	public static final int TYPE_DOUBLE_OBJECT = 0x61;
	public static final int TYPE_DOUBLE_PREMITIVE = 0x62;

	public static final int TYPE_STRING_MASKED = 0x7;
	public static final int TYPE_STRING_OBJECT = 0x71;

	private static final ArrayMap<Class<?>, Integer> allTypes = new ArrayMap<>();

	static {
		allTypes.put(boolean.class, TYPE_BOOLEAN_PREMITIVE);
		allTypes.put(Boolean.class, TYPE_BOOLEAN_OBJECT);

		allTypes.put(short.class, TYPE_SHORT_PREMITIVE);
		allTypes.put(Short.class, TYPE_SHORT_OBJECT);

		allTypes.put(int.class, TYPE_INTEGER_PREMITIVE);
		allTypes.put(Integer.class, TYPE_INTEGER_OBJECT);

		allTypes.put(long.class, TYPE_LONG_PREMITIVE);
		allTypes.put(Long.class, TYPE_LONG_OBJECT);

		allTypes.put(float.class, TYPE_FLOAT_PREMITIVE);
		allTypes.put(Float.class, TYPE_FLOAT_OBJECT);

		allTypes.put(double.class, TYPE_DOUBLE_PREMITIVE);
		allTypes.put(Double.class, TYPE_DOUBLE_OBJECT);

		allTypes.put(String.class, TYPE_STRING_OBJECT);
	}

	/**
	 * Get `type` from given class without separating primitive or object.
	 * For eg,. both of `int.class` and `Integer.class` will result `TYPE_INTEGER_MASKED`.
	 */
	public static int typeMasked(Class<?> type) {
		return type(type) >> 4; // hexa unit
	}

	/**
	 * Get `type` from given class (primitive or object).
	 * For eg,.
	 * - `int.class` will result `TYPE_INTEGER_PRIMITIVE`, but
	 * - `Integer.class` will result `TYPE_INTEGER_OBJECT`.
	 */
	public static int type(Class<?> type) {
		return allTypes.getOrDefault(type, 0);
	}
}
