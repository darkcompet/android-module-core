/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */
package tool.compet.core

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.os.PowerManager
import android.os.Process
import android.provider.MediaStore
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

/**
 * Utility class, provides common basic operations for app.
 */
object DkUtils {
	/**
	 * Throw RuntimeException with given msg.
	 */
	fun complain(format: String?, vararg args: Any?) {
		throw RuntimeException(DkStrings.format(format, *args))
	}

	/**
	 * Throw RuntimeException with given msg and location of class which call it.
	 */
	@JvmStatic
	fun complainAt(where: Any?, format: String?, vararg args: Any?) {
		var prefix = "~ "
		if (where != null) {
			var loc = if (where is Class<*>) where.name else where.javaClass.name
			loc = loc.substring(loc.lastIndexOf('.') + 1)
			prefix = loc + prefix
		}
		throw RuntimeException(prefix + DkStrings.format(format, *args))
	}

	fun sleep(millis: Long): Boolean {
		return try {
			Thread.sleep(millis)
			true
		}
		catch (e: Exception) {
			DkLogs.error(DkUtils::class.java, e)
			false
		}
	}

	@Throws(Exception::class)
	fun execCommand(command: String?) {
		val utf8Result: MutableList<String> = ArrayList()
		execCommand(command, utf8Result)
	}

	/**
	 * @param command like "php artisan migrate"
	 * @throws Exception when cannot detect OS type
	 */
	@Throws(Exception::class)
	fun execCommand(command: String?, utf8Result: MutableList<String>?) {
		val osname = System.getProperty("os.name").lowercase(Locale.getDefault())
		val processBuilder = ProcessBuilder()
		if (osname.contains("win")) {
			processBuilder.command("cmd.exe", "/c", command)
		}
		else if (osname.contains("mac")) {
			processBuilder.command("bash", "-c", command)
		}
		else if (osname.contains("nix") || osname.contains("nux") || osname.contains("aix")) {
			processBuilder.command("bash", "-c", command)
		}
		else if (osname.contains("sunos")) {
			processBuilder.command("bash", "-c", command)
		}
		else {
			throw Exception("Could not detect OS type")
		}
		if (utf8Result != null) {
			val process = processBuilder.start()
			var line: String
			val utf8Charset = Charset.forName("UTF-8")
			val reader = BufferedReader(InputStreamReader(process.inputStream, utf8Charset))
			while (reader.readLine().also { line = it } != null) {
				utf8Result.add(line)
			}
		}
	}

	fun stream2string(`is`: InputStream?): String {
		var line: String?
		val ls = DkConst.LS
		val sb = StringBuilder(256)
		try {
			BufferedReader(InputStreamReader(`is`)).use { br ->
				while (br.readLine().also { line = it } != null) {
					sb.append(line).append(ls)
				}
			}
		}
		catch (e: Exception) {
			DkLogs.error(DkUtils::class.java, e)
		}
		return sb.toString()
	}

	/**
	 * Read all content of a file under assets folder as lines.
	 *
	 * @return List of line if succeed. Otherwise return Null.
	 */
	fun asset2lines(context: Context, fileName: String?, trim: Boolean): List<String>? {
		val lines: MutableList<String> = ArrayList()
		var line: String
		try {
			BufferedReader(InputStreamReader(context.assets.open(fileName!!))).use { br ->
				while (br.readLine().also { line = it } != null) {
					lines.add(if (trim) line.trim { it <= ' ' } else line)
				}
			}
			return lines
		}
		catch (e: Exception) {
			DkLogcats.error(DkLogcats::class.java, e)
		}
		return null
	}

	/**
	 * @return File content as string if succeed. Otherwise return Null.
	 */
	fun asset2string(context: Context, fileName: String?): String? {
		return try {
			stream2string(context.assets.open(fileName!!))
		}
		catch (e: IOException) {
			DkLogcats.error(DkLogcats::class.java, e)
			null
		}
	}

	fun restartApp(context: Context, startActivity: Class<*>?) {
		val startIntent = Intent(context, startActivity)
		val pendingIntent = PendingIntent.getActivity(
			context,
			Process.myPid(),
			startIntent,
			PendingIntent.FLAG_CANCEL_CURRENT
		)
		val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		alarm?.set(AlarmManager.RTC, System.currentTimeMillis() + 200, pendingIntent)
		Runtime.getRuntime().exit(0)
	}

	fun setFullScreen(host: Activity, fullScreen: Boolean) {
		// alter at onAttach(): host.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		val addFlag =
			if (fullScreen) WindowManager.LayoutParams.FLAG_FULLSCREEN else WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
		val clearFlag =
			WindowManager.LayoutParams.FLAG_FULLSCREEN + WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN - addFlag
		val window = host.window
		if (window != null) {
			window.addFlags(addFlag)
			window.clearFlags(clearFlag)
		}
	}

	/**
	 * Dim ui system bars like: status bar, navigation bar...
	 * Note that, once user touches some system bar, you need call this to dim again.
	 */
	fun dimSystemBars(host: Activity) {
		val decorView = host.window.decorView
		val uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
		decorView.systemUiVisibility = uiOptions
	}

	fun sendEmail(context: Context, dstEmail: String, subject: String, message: String) {
		val uri = Uri.fromParts("mailto", dstEmail, null)
		val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
		emailIntent.putExtra(Intent.EXTRA_TEXT, message)
		context.startActivity(Intent.createChooser(emailIntent, "Send email"))
	}

	@Throws(IOException::class)
	fun getPhotoOrientation(photoPath: String?): Int {
		val ei = ExifInterface(photoPath!!)
		val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
		when (orientation) {
			ExifInterface.ORIENTATION_ROTATE_90 -> return 90
			ExifInterface.ORIENTATION_ROTATE_180 -> return 180
			ExifInterface.ORIENTATION_ROTATE_270 -> return 270
			ExifInterface.ORIENTATION_NORMAL -> return 0
		}
		return 0
	}

	fun getPhotoOrientation(context: Context, photoUri: Uri?): Int {
		var rotate = 0
		try {
			context.contentResolver.notifyChange(photoUri!!, null)
			val imageFile = File(getPathFromUri(context, photoUri))
			val exif = ExifInterface(imageFile.absolutePath)
			val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
			when (orientation) {
				ExifInterface.ORIENTATION_NORMAL -> rotate = 0
				ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
				ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
				ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
			}
		}
		catch (e: Exception) {
			DkLogcats.error(DkLogcats::class.java, e)
		}
		return rotate
	}

	fun getGalleryPhotoPath(context: Context, data: Intent?): String? {
		return if (data == null) {
			null
		}
		else getPathFromUri(context, data.data)
	}

	fun getPathFromUri(context: Context, uri: Uri?): String? {
		if (uri == null) {
			return null
		}
		var res: String? = null
		val proj = arrayOf(MediaStore.Images.Media.DATA)
		val cursor = context.contentResolver.query(uri, proj, null, null, null)
		if (cursor != null && cursor.moveToFirst()) {
			val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
			res = cursor.getString(column_index)
			cursor.close()
		}
		return res
	}

	fun hideSoftKeyboard(context: Context, view: View?) {
		val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		if (imm != null && view != null) {
			imm.hideSoftInputFromWindow(view.windowToken, 0)
		}
	}

	@RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
	fun isOnline(context: Context): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkInfo = connectivityManager.activeNetworkInfo
		return networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED
	}

	fun getScreenRotation(host: Activity): Int {
		val rotation = host.windowManager.defaultDisplay.rotation
		return if (host.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			when (rotation) {
				Surface.ROTATION_0, Surface.ROTATION_90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
				else -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
			}
		}
		else {
			when (rotation) {
				Surface.ROTATION_0, Surface.ROTATION_270 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
				else -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
			}
		}
	}

	fun setScreenOrientation(host: Activity, hostInfoOrientation: Int) {
		host.requestedOrientation = hostInfoOrientation
	}

	/**
	 * Also add this line to Manifest.xml: android:configChanges="keyboardHidden|orientation|screenSize"
	 */
	fun lockOrientation(host: Activity) {
		host.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
	}

	/**
	 * Store bitmap to app-internal cache directory, and return
	 * the URI path which can be used to share with another apps.
	 *
	 * Ref:
	 * - https://www.geeksforgeeks.org/how-to-share-image-of-your-app-with-another-app-in-android/
	 * - https://developer.android.com/training/sharing/send
	 *
	 * @param authority Package name of FileProvider declared at xml file.
	 * @param file Target file which be read.
	 */
	fun getUriForFile(
		context: Context,
		authority: String,
		file: File,
	): Uri? {
		return try {
			FileProvider.getUriForFile(context, authority, file)
		}
		catch (e: Exception) {
			null
		}
	}

	fun shareOn(context: Context, targetAppPkg: String?, title: String?, message: String?): Boolean {
		val intent = context.packageManager.getLaunchIntentForPackage(targetAppPkg!!) ?: return false
		val shareIntent = Intent()
		shareIntent.action = Intent.ACTION_SEND
		shareIntent.setPackage(targetAppPkg)
		shareIntent.putExtra(Intent.EXTRA_TITLE, title)
		shareIntent.putExtra(Intent.EXTRA_TEXT, message)
		context.startActivity(shareIntent)
		return true
	}

	/**
	 * @param uris URIs to files. For eg,. to make an URI from a bitmap, caller can call `createUriForBitmap()`.
	 */
	fun share(context: Context, subject: String, message: String, uris: ArrayList<Uri>? = null): Boolean {
		val intent = Intent().apply {
			this.action = Intent.ACTION_SEND
			this.type = "text/plain"
			this.putExtra(Intent.EXTRA_SUBJECT, subject);
			this.putExtra(Intent.EXTRA_TEXT, message)
		}

		// Attach files
		if (uris != null && uris.isNotEmpty()) {
			intent.action = Intent.ACTION_SEND_MULTIPLE
			intent.type = "image/*"
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
		}

		context.startActivity(Intent.createChooser(intent, "share"))

		return true
	}

	/**
	 * @return TRUE iff it is api 21+ and battery is in save-mode. Otherwise FALSE.
	 */
	fun isPowerSaveMode(context: Context): Boolean {
		val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
		return powerManager.isPowerSaveMode
	}

	@JvmStatic
	fun checkPermission(context: Context, vararg permissions: String): Boolean {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			for (permission in permissions) {
				if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
					return false
				}
			}
		}
		return true
	}

	val isMainThread: Boolean
		get() = Thread.currentThread() === Looper.getMainLooper().thread
}