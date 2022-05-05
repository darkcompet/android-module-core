package tool.compet.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class MyLogging {
	static String beautifyError(@NonNull Throwable error, @Nullable String format, Object... args) {
		// Unwrap to get origin throwable since some exception can wrap other exception for grouping
		Throwable originThrowable = error.getCause();
		if (originThrowable != null) {
			error = originThrowable;
		}

		StringBuilder sb = new StringBuilder(256);

		if (format != null) {
			if (args != null) {
				format = DkStrings.format(format, args);
			}
			sb.append("Message: ").append(format).append(DkConst.LS);
		}

		sb.append(error.toString()).append(DkConst.LS);

		for (StackTraceElement traceElement : error.getStackTrace()) {
			sb.append("\tat ").append(traceElement).append(DkConst.LS);
		}

		return sb.toString();
	}

	static String beautifyBacktrace() {
		ArrayList<String> descriptions = new ArrayList<>();
		for (StackTraceElement elm : Thread.currentThread().getStackTrace()) {
			String description = DkStrings.format("%s (%d) ==> %s.%s()", elm.getFileName(), elm.getLineNumber(), elm.getClassName(), elm.getMethodName());
			descriptions.add(description);
		}
		return "\nStack Trace:\n" + DkStrings.join('\n', descriptions);
	}
}
