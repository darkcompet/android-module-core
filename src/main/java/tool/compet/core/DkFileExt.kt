package tool.compet.core

import java.io.File
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat

//
// Please take care terminology of `file` and `dir` are different.
// When we mention `file`, that mean ONLY file is targeted.
// When we mention `dir`, that mean ONLY dir is targeted.
// When we do NOT mention `file` or `dir`, that mean both of them are targeted.
//

/**
 * Check `file` was not exist, if such that create new file.
 * For safely, operations are wrapped with try/catch.
 *
 * @return TRUE if `file was existed and normal`, OR `new file was created`.
 */
fun File.createFileDk() : Boolean {
	try {
		if (isFile) { // File existed and normal.
			return true
		}
		// Check and Create parent dir if not exist
		val parent: File? = this.parentFile
		if (parent != null && !parent.exists() && !parent.mkdirs()) {
			return false
		}
		return createNewFile()
	}
	catch (e: Exception) {
		DkLogs.error(this, e, "Could not createFileDk")
	}
	return false
}

/**
 * When file/dir exists, this will check it is empty (no byte, no child) or not.
 *
 * @return TRUE if this file/dir is `normal existing` AND `nobyte/nochild`.
 */
fun File.emptyDk() : Boolean {
	return (isFile && length() == 0L) || (isDirectory && list().isNullOrEmpty())
}

//
// Please take care terminology of `file` and `dir` are different.
// When we mention `file`, that mean ONLY file is targeted.
// When we mention `dir`, that mean ONLY dir is targeted.
// When we do NOT mention `file` or `dir`, that mean both of them are targeted.
//

fun File.isReadOnlyDk(context: Context) = canRead() && !isWritableDk(context)

/**
 * Use it, because [File.canWrite] is unreliable on Android 10.
 * Read [this issue](https://github.com/anggrayudi/SimpleStorage/issues/24#issuecomment-830000378)
 */
fun File.isWritableDk(context: Context) = canWrite() && (isFile || isExternalStorageManagerDk(context))

/**
 * For Q+, this will check with `Environment.isExternalStorageManager`.
 * To gain full access, the app should request permission `Manifest.permission.MANAGE_EXTERNAL_STORAGE` via
 * setting of `android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION`.
 *
 * @return TRUE if we have full disk access.
 */
fun File.isExternalStorageManagerDk(context: Context) : Boolean {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
		return Environment.isExternalStorageManager(this)
	}
	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
		@Suppress("DEPRECATION")
		val externalStoragePath = Environment.getExternalStorageDirectory().absolutePath

		return path.startsWith(externalStoragePath)
			&& ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
			&& ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
	}

	val externalDirs = mutableListOf<File>()
	externalDirs.addAll(ContextCompat.getObbDirs(context).filterNotNull())
	externalDirs.addAll(ContextCompat.getExternalFilesDirs(context, null).mapNotNull { it?.parentFile })

	return externalDirs.any { path.startsWith(it.path) }
}
