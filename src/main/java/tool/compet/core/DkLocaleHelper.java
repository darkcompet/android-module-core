/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class DkLocaleHelper {
	/**
	 * Change locale of given context.
	 *
	 * @param context Current context
	 * @param langCode Next language to change, for eg,. vn, jp,...
	 * @return Localed context
	 */
	public static ContextWrapper changeLocale(Context context, String langCode) {
		final Locale systemLocale = DkConfig.appLocale(context);

		if (! DkConst.EMPTY_STRING.equals(langCode) && ! systemLocale.getLanguage().equals(langCode)) {
			Configuration config = context.getResources().getConfiguration();
			Locale locale = new Locale(langCode);
			Locale.setDefault(locale);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				config.setLocale(locale);
			}
			else {
				config.locale = locale;
			}

			context = context.createConfigurationContext(config);
		}

		return new ContextWrapper(context);
	}
}
