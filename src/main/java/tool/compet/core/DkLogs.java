/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * System console standard log. It also provide benchmark for debugging.
 * Because a lot of places call this log, so this does not provide log-callback
 * to avoid infinitive looping.
 */
public class DkLogs implements DkLogger.LogType {
	public static DkLogger logger;

	// Enable this to log back trace of current thread
	public static boolean logBackTrace;

	// For benchmark
	private static ArrayDeque<Object[]> benchmarkTaskQueue;

	public static DkLogger logger() {
		return logger != null ? logger : (logger = new DkLogger(DkLogs::logActual));
	}

	/**
	 * Log for testing, don't use at any env of apps or libraries.
	 */
	public static void test(@Nullable Object where, @Nullable String format, Object... args) {
		System.out.println(logger().makePrefix(DkLogger.LogType.TYPE_DEBUG, where) + DkStrings.format(format, args));
	}

	/**
	 * Debug log. Only run at debug env, so should wrap it with DEBUG.
	 * Notice: should remove all debug code when release.
	 */
	public static void debug(@Nullable Object where, @Nullable String format, Object... args) {
		logger().debug(where, format, args);
	}

	/**
	 * Log info. Only run at debug env, so should wrap it with DEBUG.
	 */
	public static void info(@Nullable Object where, @Nullable String format, Object... args) {
		logger().info(where, format, args);
	}

	/**
	 * Log notice. Only run at debug env, so should wrap it with DEBUG.
	 */
	public static void notice(@Nullable Object where, @Nullable String format, Object... args) {
		logger().notice(where, format, args);
	}

	/**
	 * Warning log. Run at both debug and production env, no wrapping is needed.
	 */
	public static void warning(@Nullable Object where, @Nullable String format, Object... args) {
		logger().warning(where, format, args);
	}

	/**
	 * Error log. Run at both debug and production env, no wrapping is needed.
	 */
	public static void error(@Nullable Object where, @Nullable String format, Object... args) {
		logger().error(where, format, args);
	}

	/**
	 * Exception log. Run at both debug and production env, no wrapping is needed.
	 */
	public static void error(@Nullable Object where, Throwable e) {
		error(where, e, null);
	}

	/**
	 * Exception log. Run at both debug and production env, no wrapping is needed.
	 */
	public static void error(@Nullable Object where, Throwable e, @Nullable String format, Object... args) {
		logger().error(where, e, format, args);
	}

	/**
	 * Critical log. Run at both debug and production env, no wrapping is needed.
	 */
	public static void critical(@Nullable Object where, @Nullable String format, Object... args) {
		logger().critical(where, format, args);
	}

	/**
	 * Emergency log. Run at both debug and production env, no wrapping is needed.
	 */
	public static void emergency(@Nullable Object where, @Nullable String format, Object... args) {
		logger().emergency(where, format, args);
	}

	/**
	 * Log with specific log-type.
	 */
	public static void log(Object where, int type, String format, Object... args) {
		if (type == TYPE_DEBUG) {
			debug(where, format, args);
		}
		else if (type == TYPE_INFO) {
			info(where, format, args);
		}
		else if (type == TYPE_NOTICE) {
			notice(where, format, args);
		}
		else if (type == TYPE_WARNING) {
			warning(where, format, args);
		}
		else if (type == TYPE_ERROR) {
			error(where, format, args);
		}
		else if (type == TYPE_CRITICAL) {
			critical(where, format, args);
		}
		else if (type == TYPE_EMERGENCY) {
			emergency(where, format, args);
		}
		else {
			throw new RuntimeException("Invalid log type: " + type);
		}
	}

	/**
	 * Start benchmark. Only run at debug env, so should wrap it with DEBUG.
	 */
	public static void tick(@Nullable Object where, String task) {
		if (BuildConfig.DEBUG) {
			if (benchmarkTaskQueue == null) {
				benchmarkTaskQueue = new ArrayDeque<>();
			}
			benchmarkTaskQueue.push(new Object[] {task, System.currentTimeMillis()});
			logger().debug(where, "Start task [%s]", task);;
		}
	}

	/**
	 * End benchmark. Only run at debug env, so should wrap it with DEBUG.
	 */
	public static void tock(@Nullable Object where) {
		if (BuildConfig.DEBUG) {
			Object[] taskInfo = benchmarkTaskQueue.pop();
			logger().debug(where, "End task [%s] in %d ms", (String) taskInfo[0], System.currentTimeMillis() - (long) taskInfo[1]);
		}
	}

	private static void logActual(int logType, String message) {
		if (logBackTrace) {
			ArrayList<String> descriptions = new ArrayList<>();
			for (StackTraceElement elm : Thread.currentThread().getStackTrace()) {
				String description = DkStrings.format("%s (%d) ==> %s.%s()", elm.getFileName(), elm.getLineNumber(), elm.getClassName(), elm.getMethodName());
				descriptions.add(description);
			}
			String trace = DkStrings.join('\n', descriptions);
			message += "\nStack Trace:\n" + trace;
		}

		String logName = DkLogger.LogType.name(logType);

		switch (logType) {
			case TYPE_DEBUG:
			case TYPE_INFO:
			case TYPE_NOTICE: {
				System.out.println("dklog_" + logName + ": " + message);
				break;
			}
			case TYPE_WARNING:
			case TYPE_ERROR:
			case TYPE_CRITICAL:
			case TYPE_EMERGENCY: {
				System.err.println("dklog_" + logName + ": " + message);
				break;
			}
		}
	}
}
