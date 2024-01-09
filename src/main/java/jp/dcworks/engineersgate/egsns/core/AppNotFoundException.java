package jp.dcworks.engineersgate.egsns.core;

/**
 * NotFound時に使用する例外クラス。
 *
 * @author tomo-sato
 */
public class AppNotFoundException extends RuntimeException {

	public AppNotFoundException() {
		super();
	}

	public AppNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AppNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppNotFoundException(String message) {
		super(message);
	}

	public AppNotFoundException(Throwable cause) {
		super(cause);
	}
}
