/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */
package tool.compet.core

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*

object DkLocaleHelper {
	/**
	 * Change locale of given context.
	 *
	 * @param _context Current context
	 * @param langCode Next language to change, for eg,. vn, jp,...
	 * @return Localed context
	 */
	fun changeLocale(_context: Context, langCode: String): ContextWrapper {
		var context = _context
		val systemLocale = DkConfig.appLocale(context)

		if (DkConst.EMPTY_STRING != langCode && systemLocale.language != langCode) {
			val config = context.resources.configuration
			val locale = Locale(langCode)

			Locale.setDefault(locale)
			config.setLocale(locale)

			context = context.createConfigurationContext(config)
		}

		return ContextWrapper(context)
	}
}