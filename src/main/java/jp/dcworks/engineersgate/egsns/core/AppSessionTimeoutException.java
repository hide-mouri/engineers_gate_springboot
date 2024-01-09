package jp.dcworks.engineersgate.egsns.core;

/**
 * セッションタイムアウト時に使用する例外クラス。
 *
 * @author tomo-sato
 */
public class AppSessionTimeoutException extends AppNotFoundException {

	public AppSessionTimeoutException() {
		super();
	}

	public AppSessionTimeoutException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AppSessionTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppSessionTimeoutException(String message) {
		super(message);
	}

	public AppSessionTimeoutException(Throwable cause) {
		super(cause);
	}
}
