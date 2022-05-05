package tool.compet.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * We use {@link Intent} to request other app's activity do some job for us.
 * For convenience, developer can get intent provided from this class,
 * setup with extra info to match with the requirement.
 */
public class DkIntents {
	/**
	 * Start system-settings activity to open setting page.
	 * Use this to ask user do something for the app, for eg,. turn on permission,...
	 */
	public static Intent getIntentForOpenAppSettingPage(String packageName) {
		Uri uri = Uri.fromParts("package", packageName, null);

		return new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(uri);
	}

	public static Intent getIntentForUninstallPackage(String packageName) {
		return new Intent(Intent.ACTION_UNINSTALL_PACKAGE)
			.setData(Uri.parse("package:" + packageName))
			.putExtra(Intent.EXTRA_RETURN_RESULT, true);
	}

	/**
	 * @return intent for app in local. If null, intent of app on play store will be returned.
	 */
	public static Intent getIntentForOpenAppInLocal(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent == null) {
			intent = getIntentForOpenAppInPlayStore(packageName);
		}
		return intent.setFlags(0);
	}

	public static Intent getIntentForOpenAppInPlayStore(String packageName) {
		return new Intent(Intent.ACTION_VIEW)
			.setData(Uri.parse("market://details?id=" + packageName))
			.setFlags(0);
	}

	public static Intent getIntentForRateApp(Context context) {
		try {
			Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

			if (Build.VERSION.SDK_INT >= 21) {
				flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
			}
			else {
				flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
			}
			return intent.addFlags(flags);
		}
		catch (Exception e) {
			DkLogcats.error(DkLogcats.class, e);
		}
		return new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google" + ".com/store/apps/details?id=" + context.getPackageName()));
	}

	public static Intent getIntentForDeveloperAppListInPlayStore(String developerName) {
		try {
			Uri uri = Uri.parse("market://search?q=pub:" + developerName);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
			}
			else {
				flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
			}
			return intent.addFlags(flags);
		}
		catch (Exception e) {
			DkLogcats.error(DkLogcats.class, e);
		}
		return new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + developerName));
	}
}
