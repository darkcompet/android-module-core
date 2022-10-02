/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.annotation.Nullable;

public class DkLogger {
	public interface LogType {
		int TYPE_DEBUG = 1;
		int TYPE_INFO = 2;
		int TYPE_NOTICE = 3;
		int TYPE_WARNING = 4;
		int TYPE_ERROR = 5;
		int TYPE_CRITICAL = 6;
		int TYPE_EMERGENCY = 7;

		static String name(int type) {
			switch (type) {
				// Below 3 log types should be considered as debug
				case LogType.TYPE_DEBUG:
					return "debug";
				case LogType.TYPE_INFO:
					return "info";
				case LogType.TYPE_NOTICE:
					return "notice";
				// Below 4 log types should be considered as error
				case LogType.TYPE_WARNING:
					return "warning";
				case LogType.TYPE_ERROR:
					return "error";
				case LogType.TYPE_CRITICAL:
					return "critical";
				case LogType.TYPE_EMERGENCY:
					return "emergency";
			}
			throw new RuntimeException("Invalid log type: " + type);
		}
	}

	/**
	 * Log Printer.
	 */
	public interface LogImpl {
		void log(int logType, String message);
	}

	// Log adapter for actual log
	private LogImpl logImpl;

	public DkLogger(LogImpl logImpl) {
		this.logImpl = logImpl;
	}

	public void setLogImpl(LogImpl logImpl) {
		this.logImpl = logImpl;
	}

	/**
	 * Log debug.
	 */
	public void debug(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_DEBUG, where, format, args);
	}

	/**
	 * Log info.
	 */
	public void info(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_INFO, where, format, args);
	}

	/**
	 * Log notice.
	 */
	public void notice(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_NOTICE, where, format, args);
	}

	/**
	 * Log warning.
	 */
	public void warning(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_WARNING, where, format, args);
	}

	/**
	 * Log error.
	 */
	public void error(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_ERROR, where, format, args);
	}

	/**
	 * Log exception.
	 */
	public void error(@Nullable Object where, Throwable e) {
		error(where, e, null);
	}

	/**
	 * Log exception.
	 */
	public void error(@Nullable Object where, Throwable e, @Nullable String format, Object... args) {
		log(LogType.TYPE_ERROR, where, MyLogging.beautifyError(e, format, args));
	}

	/**
	 * Log critical.
	 */
	public void critical(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_CRITICAL, where, format, args);
	}

	/**
	 * Log emergency.
	 */
	public void emergency(@Nullable Object where, @Nullable String format, Object... args) {
		log(LogType.TYPE_EMERGENCY, where, format, args);
	}

	// region: Protected

	protected void log(int logType, @Nullable Object where, @Nullable String format, Object... args) {
		String message = format;
		if (args != null && args.length > 0) {
			message = DkStrings.format(format, args);
		}

		logImpl.log(logType, makePrefix(logType, where) + message);
	}

	protected String makePrefix(int logType, @Nullable Object where) {
		String prefix = "~ ";
		if (logType >= LogType.TYPE_NOTICE) {
			prefix += "-----> ";
		}

		if (where != null) {
			String loc;
			if (where instanceof String) {
				loc = (String) where;
			}
			else if (where instanceof Class) {
				loc = ((Class<?>) where).getSimpleName();
				loc = loc.substring(loc.lastIndexOf('.') + 1);
			}
			else {
				loc = where.getClass().getSimpleName();
			}
			return (loc + prefix);
		}
		return prefix;
	}

	// endregion: Protected
}
