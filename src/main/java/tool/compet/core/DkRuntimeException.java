package tool.compet.core;

public class DkRuntimeException extends RuntimeException {
	public DkRuntimeException(Object where, String format, Object... args) {
		super("dklog: " + where.getClass().getName() + "~ [Must review] " + DkStrings.format(format, args));
	}

	public DkRuntimeException(Object where, Throwable e, String format, Object... args) {
		super("dklog: " + where.getClass().getName() + "~ [Must review] " + MyLogging.beautifyError(e, format, args));
	}
}
