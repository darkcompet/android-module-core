/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import static android.view.WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class, provides common basic operations for app.
 */
public class DkUtils {
	/**
	 * Throw RuntimeException with given msg.
	 */
	public static void complain(String format, Object... args) {
		throw new RuntimeException(DkStrings.format(format, args));
	}

	/**
	 * Throw RuntimeException with given msg and location of class which call it.
	 */
	public static void complainAt(Object where, String format, Object... args) {
		String prefix = "~ ";
		if (where != null) {
			String loc = (where instanceof Class) ? ((Class) where).getName() : where.getClass().getName();
			loc = loc.substring(loc.lastIndexOf('.') + 1);
			prefix = loc + prefix;
		}
		throw new RuntimeException(prefix + DkStrings.format(format, args));
	}

	public static boolean sleep(long millis) {
		try {
			Thread.sleep(millis);
			return true;
		} catch (Exception e) {
			DkLogs.error(DkUtils.class, e);
			return false;
		}
	}

	public static void execCommand(String command) throws Exception {
		List<String> utf8Result = new ArrayList<>();
		execCommand(command, utf8Result);
	}

	/**
	 * @param command like "php artisan migrate"
	 * @throws Exception when cannot detect OS type
	 */
	public static void execCommand(String command, List<String> utf8Result) throws Exception {
		String osname = System.getProperty("os.name").toLowerCase();
		ProcessBuilder processBuilder = new ProcessBuilder();

		if (osname.contains("win")) {
			processBuilder.command("cmd.exe", "/c", command);
		} else if (osname.contains("mac")) {
			processBuilder.command("bash", "-c", command);
		} else if (osname.contains("nix") || osname.contains("nux") || osname.contains("aix")) {
			processBuilder.command("bash", "-c", command);
		} else if (osname.contains("sunos")) {
			processBuilder.command("bash", "-c", command);
		} else {
			throw new Exception("Could not detect OS type");
		}

		if (utf8Result != null) {
			java.lang.Process process = processBuilder.start();
			String line;
			Charset utf8Charset = Charset.forName("UTF-8");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), utf8Charset));

			while ((line = reader.readLine()) != null) {
				utf8Result.add(line);
			}
		}
	}

	public static String stream2string(InputStream is) {
		String line;
		String ls = DkConst.LS;
		StringBuilder sb = new StringBuilder(256);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			while ((line = br.readLine()) != null) {
				sb.append(line).append(ls);
			}
		} catch (Exception e) {
			DkLogs.error(DkUtils.class, e);
		}

		return sb.toString();
	}

	/**
	 * Read all content of a file under assets folder as lines.
	 *
	 * @return List of line if succeed. Otherwise return Null.
	 */
	@Nullable
	public static List<String> asset2lines(Context context, String fileName, boolean trim) {
		List<String> lines = new ArrayList<>();
		String line;

		try {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)))) {
				while ((line = br.readLine()) != null) {
					lines.add(trim ? line.trim() : line);
				}
			}
			return lines;
		} catch (Exception e) {
			DkLogcats.error(DkLogcats.class, e);
		}
		return null;
	}

	/**
	 * @return File content as string if succeed. Otherwise return Null.
	 */
	@Nullable
	public static String asset2string(Context context, String fileName) {
		try {
			return stream2string(context.getAssets().open(fileName));
		} catch (IOException e) {
			DkLogcats.error(DkLogcats.class, e);
			return null;
		}
	}

	public static void restartApp(Context context, Class startActivity) {
		Intent startIntent = new Intent(context, startActivity);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
			Process.myPid(),
			startIntent,
			PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		if (alarm != null) {
			alarm.set(AlarmManager.RTC, System.currentTimeMillis() + 200, pendingIntent);
		}

		Runtime.getRuntime().exit(0);
	}

	public static void setFullScreen(Activity host, boolean fullScreen) {
		// alter at onAttach(): host.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

		int addFlag = fullScreen ? FLAG_FULLSCREEN : FLAG_FORCE_NOT_FULLSCREEN;
		int clearFlag = FLAG_FULLSCREEN + FLAG_FORCE_NOT_FULLSCREEN - addFlag;

		Window window = host.getWindow();
		if (window != null) {
			window.addFlags(addFlag);
			window.clearFlags(clearFlag);
		}
	}

	/**
	 * Dim ui system bars like: status bar, navigation bar...
	 * Note that, once user touches some system bar, you need call this to dim again.
	 */
	public static void dimSystemBars(Activity host) {
		View decorView = host.getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;

		decorView.setSystemUiVisibility(uiOptions);
	}

	public static void sendEmail(Context context, String dstEmail, String subject, String message) {
		Uri uri = Uri.fromParts("mailto", dstEmail, null);
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, message);
		context.startActivity(Intent.createChooser(emailIntent, "Send email"));
	}

	public static int getPhotoOrientation(String photoPath) throws IOException {
		ExifInterface ei = new ExifInterface(photoPath);
		int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

		switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				return 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				return 180;
			case ExifInterface.ORIENTATION_ROTATE_270:
				return 270;
			case ExifInterface.ORIENTATION_NORMAL:
				return 0;
		}
		return 0;
	}

	public static int getPhotoOrientation(Context context, Uri photoUri) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(photoUri, null);
			File imageFile = new File(getPathFromUri(context, photoUri));
			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
			switch (orientation) {
				case ExifInterface.ORIENTATION_NORMAL:
					rotate = 0;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					rotate = 270;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					rotate = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_90:
					rotate = 90;
					break;
			}
		} catch (Exception e) {
			DkLogcats.error(DkLogcats.class, e);
		}

		return rotate;
	}

	public static String getGalleryPhotoPath(Context context, @Nullable Intent data) {
		if (data == null) {
			return null;
		}
		return getPathFromUri(context, data.getData());
	}

	public static String getPathFromUri(Context context, @Nullable Uri uri) {
		if (uri == null) {
			return null;
		}

		String res = null;
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
			cursor.close();
		}

		return res;
	}

	public static void hideSoftKeyboard(Context context, @Nullable View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && view != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	@RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
	public static boolean isOnline(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
	}

	public static int getScreenRotation(Activity host) {
		int rotation = host.getWindowManager().getDefaultDisplay().getRotation();
		if (host.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			switch (rotation) {
				case Surface.ROTATION_0:
				case Surface.ROTATION_90:
					return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				default:
					return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
			}
		} else {
			switch (rotation) {
				case Surface.ROTATION_0:
				case Surface.ROTATION_270:
					return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				default:
					return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
			}
		}
	}

	public static void setScreenOrientation(Activity host, int hostInfoOrientation) {
		host.setRequestedOrientation(hostInfoOrientation);
	}

	/**
	 * Also add this line to Manifest.xml: android:configChanges="keyboardHidden|orientation|screenSize"
	 */
	public static void lockOrientation(Activity host) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			host.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
		}
	}

	public static boolean shareOn(Context context, String targetAppPkg, String title, String message) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(targetAppPkg);
		if (intent == null) {
			return false;
		}
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setPackage(targetAppPkg);

		shareIntent.putExtra(Intent.EXTRA_TITLE, title);
		shareIntent.putExtra(Intent.EXTRA_TEXT, message);

		context.startActivity(shareIntent);

		return true;
	}

	/**
	 * Caller must grant permission `DkConst.WRITE_EXTERNAL_STORAGE`.
	 */
	public static boolean share(Context context, String message, Iterable<Bitmap> bitmaps) {
		if (!DkUtils.checkPermission(context, DkConst.WRITE_EXTERNAL_STORAGE)) {
			return false;
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, message);

		ArrayList<Uri> uris = new ArrayList<>();

		for (Bitmap bitmap : bitmaps) {
			if (bitmap != null) {
				String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "share", null);
				Uri uri = Uri.parse(bitmapPath);
				uris.add(uri);
			}
		}
		if (uris.size() > 0) {
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			intent.setType("image/*");
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		}

		context.startActivity(Intent.createChooser(intent, "share"));

		return true;
	}

	/**
	 * @return TRUE iff it is api 21+ and battery is in save-mode. Otherwise FALSE.
	 */
	public static boolean isPowerSaveMode(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			return powerManager.isPowerSaveMode();
		}
		return false;
	}

	public static boolean checkPermission(Context context, String... permissions) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
			for (String permission : permissions) {
				if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isMainThread() {
		return Thread.currentThread() == Looper.getMainLooper().getThread();
	}
}
