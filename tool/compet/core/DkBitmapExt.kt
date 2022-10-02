package tool.compet.core

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import java.io.ByteArrayOutputStream

/**
 * Calculate size (byte count) of given bitmap.
 */
fun Bitmap?.sizeDk() : Long {
	return when {
		this == null -> {
			0L
		}
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
			this.allocationByteCount.toLong()
		}
		else -> {
			this.byteCount.toLong()
		}
	}
}

/**
 * Convert this bitmap to array of byte.
 */
fun Bitmap.toByteArrayDk(): ByteArray {
	return ByteArrayOutputStream().let { baos ->
		this.compress(Bitmap.CompressFormat.PNG, 100, baos)
		baos.toByteArray()
	}
}

/**
 * @param degrees Rotation angle in degress in clockwise.
 */
fun Bitmap.rotateDk(degrees: Int): Bitmap {
	val matrix = Matrix().apply {
		this.postRotate(degrees.toFloat())
	}
	return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}