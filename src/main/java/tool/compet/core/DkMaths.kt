/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */
package tool.compet.core

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.hypot

/**
 * This class, provides common basic operations for math.
 */
object DkMaths {
	private const val PIPI = PI * 2

	fun min(vararg args: Int): Int {
		var min = args[0]
		for (x in args) {
			if (min > x) {
				min = x
			}
		}
		return min
	}

	fun min(vararg args: Long): Long {
		var min = args[0]
		for (x in args) {
			if (min > x) {
				min = x
			}
		}
		return min
	}

	fun min(vararg args: Float): Float {
		var min = args[0]
		for (x in args) {
			if (min > x) {
				min = x
			}
		}
		return min
	}

	fun min(vararg args: Double): Double {
		var min = args[0]
		for (x in args) {
			if (min > x) {
				min = x
			}
		}
		return min
	}

	fun max(vararg args: Int): Int {
		var max = args[0]
		for (x in args) {
			if (max < x) {
				max = x
			}
		}
		return max
	}

	fun max(vararg args: Long): Long {
		var max = args[0]
		for (x in args) {
			if (max < x) {
				max = x
			}
		}
		return max
	}

	fun max(vararg args: Float): Float {
		var max = args[0]
		for (x in args) {
			if (max < x) {
				max = x
			}
		}
		return max
	}

	fun max(vararg args: Double): Double {
		var max = args[0]
		for (x in args) {
			if (max < x) {
				max = x
			}
		}
		return max
	}

	/**
	 * Support for Math, this is for degrees-angle (not radian-angle).
	 */
	fun sin(degrees: Double): Double {
		return Math.sin(Math.PI * degrees / 180.0)
	}

	/**
	 * Support for Math, this is for degrees-angle (not radian-angle).
	 */
	fun cos(degrees: Double): Double {
		return Math.cos(Math.PI * degrees / 180.0)
	}

	/**
	 * Support for Math, this is for degrees-angle (not radian-angle).
	 */
	fun tan(degrees: Double): Double {
		return Math.tan(Math.PI * degrees / 180.0)
	}

	/**
	 * @return x^n
	 */
	fun fastPow(x: Int, n: Int): Int {
		var x = x
		var n = n
		if (n == 0) {
			return if (x == 0) 0 else 1
		}
		if (n < 0) {
			return 0
		}
		var res = 1

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if (n and 1 == 1) {
				res *= x
			}
			// check bit from right to left
			if (1.let { n = n shr it; n } > 0) {
				x *= x
			}
		}
		return res
	}

	/**
	 * @return x^n
	 */
	fun fastPow(x: Long, n: Int): Long {
		var x = x
		var n = n
		if (n == 0) {
			return 1
		}
		if (n < 0) {
			return 0
		}
		var res: Long = 1

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if (n and 1 == 1) {
				res *= x
			}
			// check bit from right to left
			if (1.let { n = n shr it; n } > 0) {
				x *= x
			}
		}
		return res
	}

	/**
	 * @return x^n
	 */
	fun fastPow(_x: Float, _n: Int): Float {
		var x = _x
		var n = _n
		if (n == 0) {
			return 1f
		}
		if (n < 0) {
			return 1f / fastPow(x, -n)
		}
		var res = 1f

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if (n and 1 == 1) {
				res *= x
			}
			// check bit from right to left
			if (1.let { n = n shr it; n } > 0) {
				x *= x
			}
		}
		return res
	}

	/**
	 * @return x^n
	 */
	fun fastPow(_x: Double, _n: Int): Double {
		var x = _x
		var n = _n
		if (n == 0) {
			return 1.0
		}
		if (n < 0) {
			return 1.0 / fastPow(x, -n)
		}
		var res = 1.0

		// Express n as bit sequence: 101101, then res = x * x^4 * x^8 * x^32
		while (n > 0) {
			// multiple res with current x for bit one
			if (n and 1 == 1) {
				res *= x
			}
			// check bit from right to left
			if (1.let { n = n shr it; n } > 0) {
				x *= x
			}
		}
		return res
	}

	/**
	 * Reduce given degrees to a value in range [-180, 180].
	 */
	fun reduceToDefaultRange(degrees: Float): Float {
		var degrees = degrees
		if (degrees > 360 || degrees < -360) {
			degrees %= 360f
		}
		if (degrees > 180) {
			degrees -= 360f
		}
		if (degrees < -180) {
			degrees += 360f
		}
		return degrees
	}

	/**
	 * Calculate percentage of given value in given range [min, max] (maybe [from, to] or [to, from]).
	 *
	 * @param value For eg,. 5
	 * @param from  For eg,. 10 (or 1)
	 * @param to    For eg,. 1 (or 10)
	 * @return Nomarlized value, normally it is in range [0, 1].
	 */
	fun normalize(value: Double, from: Double, to: Double): Double {
		val min = Math.min(from, to)
		val max = Math.max(from, to)
		return (value - min) / (max - min)
	}

	/**
	 * @return Given value if it is in range [min, max]. Otherwise return min or max.
	 */
	fun clamp(value: Int, min: Int, max: Int): Int {
		return Math.max(min, Math.min(max, value))
	}

	/**
	 * @return Given value if it is in range [min, max]. Otherwise return min or max.
	 */
	@JvmStatic
	fun clamp(value: Float, min: Float, max: Float): Float {
		return Math.max(min, Math.min(max, value))
	}

	/**
	 * @return Given value if it is in range [min, max]. Otherwise return min or max.
	 */
	fun clamp(value: Double, min: Double, max: Double): Double {
		return Math.max(min, Math.min(max, value))
	}

	/**
	 * Calculate distance from given point (x, y) to line pass through (x1, y1) and (x2, y2).
	 * - Line's formula: (y2 - y1) * x + (x1 - x2) * y + x2 * y1 - x1 * y2 = 0.
	 *
	 *
	 * - Distance's formula: abc(a*x0 + b*y0 + c) / hypot(a, b)
	 * in here, line has formula: a*x + b*y + c = 0.
	 *
	 *
	 * Ref: https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
	 */
	fun distanceToLine(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float): Double {
		return Math.abs((y2 - y1) * x + (x1 - x2) * y + x2 * y1 - x1 * y2) / Math.hypot(
			(x1 - x2).toDouble(),
			(y1 - y2).toDouble()
		)
	}

	/**
	 * @return Angle (radian) in range [-pi, pi].
	 */
	fun makePiPiRange(_angle: Double): Double {
		var angle = _angle
		val PIPI = 2 * Math.PI
		if (angle >= PIPI || angle <= -PIPI) {
			angle %= PIPI
		}
		if (angle > Math.PI) {
			angle -= PIPI
		}
		if (angle < -Math.PI) {
			angle += PIPI
		}
		return angle
	}

	/**
	 * The problem when we calculate rotation angle from 2 vectors
	 * is when rotation go around angle PI, sign of angle will be changed.
	 * For eg,. 178° -> -179°, or -178° -> 179°.
	 *
	 * This function does not just compute rotation (`current_bearing - last_bearing`),
	 * but also check/fix rotation by compare with PI or -PI.
	 */
	fun calcRotationInRad(lastAngleInRad: Float, curAngleInRad: Float): Float {
		val rotation = curAngleInRad - lastAngleInRad
		if (rotation >= PI) {
			return (rotation - PIPI).toFloat()
		}

		return if (rotation <= -PI) {
			(rotation + PIPI).toFloat()
		}
		else rotation
	}

	fun calcDegreesBetweenVectors(x1: Float, y1: Float, x2: Float, y2: Float): Float {
		// u.v = |u|.|v|.cos(radian)
		val uv = x1 * x2 + y1 * y2
		val u = hypot(x1, y1)
		val v = hypot(x2, y2)

		return Math.toDegrees(acos(uv / u / v).toDouble()).toFloat()
	}
}