/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

/**
 * This class, provides common basic operations for math.
 */
public class DkMaths {
	public static int min(int... args) {
		int min = args[0];

		for (int x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static long min(long... args) {
		long min = args[0];

		for (long x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static float min(float... args) {
		float min = args[0];

		for (float x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static double min(double... args) {
		double min = args[0];

		for (double x : args) {
			if (min > x) {
				min = x;
			}
		}
		return min;
	}

	public static int max(int... args) {
		int max = args[0];

		for (int x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static long max(long... args) {
		long max = args[0];

		for (long x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static float max(float... args) {
		float max = args[0];

		for (float x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	public static double max(double... args) {
		double max = args[0];

		for (double x : args) {
			if (max < x) {
				max = x;
			}
		}
		return max;
	}

	/**
	 * Support for Math, this is for degrees-angle (not radian-angle).
	 */
	public static double sin(double degrees) {
		return Math.sin(Math.PI * degrees / 180.0);
	}

	/**
	 * Support for Math, this is for degrees-angle (not radian-angle).
	 */
	public static double cos(double degrees) {
		return Math.cos(Math.PI * degrees / 180.0);
	}

	/**
	 * Support for Math, this is for degrees-angle (not radian-angle).
	 */
	public static double tan(double degrees) {
		return Math.tan(Math.PI * degrees / 180.0);
	}

	/**
	 * @return x^n
	 */
	public static int fastPow(int x, int n) {
		if (n == 0) {
			return x == 0 ? 0 : 1;
		}
		if (n < 0) {
			return 0;
		}

		int res = 1;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				res *= x;
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				x *= x;
			}
		}

		return res;
	}

	/**
	 * @return x^n
	 */
	public static long fastPow(long x, int n) {
		if (n == 0) {
			return 1;
		}
		if (n < 0) {
			return 0;
		}

		long res = 1;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				res *= x;
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				x *= x;
			}
		}

		return res;
	}

	/**
	 * @return x^n
	 */
	public static float fastPow(float x, int n) {
		if (n == 0) {
			return 1;
		}
		if (n < 0) {
			return 1f / fastPow(x, -n);
		}

		float res = 1f;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				res *= x;
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				x *= x;
			}
		}

		return res;
	}

	/**
	 * @return x^n
	 */
	public static double fastPow(double x, int n) {
		if (n == 0) {
			return 1;
		}
		if (n < 0) {
			return 1.0 / fastPow(x, -n);
		}

		double res = 1;

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if ((n & 1) == 1) {
				res *= x;
			}
			// check bit from right to left
			if ((n >>= 1) > 0) {
				x *= x;
			}
		}

		return res;
	}

	/**
	 * Reduce given degrees to a value in range [-180, 180].
	 */
	public static float reduceToDefaultRange(float degrees) {
		if (degrees > 360 || degrees < -360) {
			degrees %= 360;
		}
		if (degrees > 180) {
			degrees -= 360;
		}
		if (degrees < -180) {
			degrees += 360;
		}
		return degrees;
	}

	/**
	 * Calculate percentage of given value in given range [min, max] (maybe [from, to] or [to, from]).
	 *
	 * @param value For eg,. 5
	 * @param from For eg,. 10 (or 1)
	 * @param to For eg,. 1 (or 10)
	 * @return Nomarlized value, normally it is in range [0, 1].
	 */
	public static double normalize(double value, double from, double to) {
		double min = Math.min(from, to);
		double max = Math.max(from, to);

		return (value - min) / (max - min);
	}

	/**
	 * @return Given value if it is in range [min, max]. Otherwise return min or max.
	 */
	public static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * @return Given value if it is in range [min, max]. Otherwise return min or max.
	 */
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * @return Given value if it is in range [min, max]. Otherwise return min or max.
	 */
	public static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Calculate distance from given point (x, y) to line pass through (x1, y1) and (x2, y2).
	 * - Line's formula: (y2 - y1) * x + (x1 - x2) * y + x2 * y1 - x1 * y2 = 0.
	 *
	 * - Distance's formula: abc(a*x0 + b*y0 + c) / hypot(a, b)
	 * in here, line has formula: a*x + b*y + c = 0.
	 *
	 * Ref: https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
	 */
	public static double distanceToLine(float x, float y, float x1, float y1, float x2, float y2) {
		return Math.abs((y2 - y1) * x + (x1 - x2) * y + x2 * y1 - x1 * y2) / Math.hypot(x1 - x2, y1 - y2);
	}

	/**
	 * @return Angle (radian) in range [-pi, pi].
	 */
	public static double makePiPiRange(double angle) {
		final double PIPI = 2 * Math.PI;
		if (angle >= PIPI || angle <= -PIPI) {
			angle %= PIPI;
		}
		if (angle > Math.PI) {
			angle -= PIPI;
		}
		if (angle < -Math.PI) {
			angle += PIPI;
		}
		return angle;
	}
}
