package jp.dcworks.engineersgate.egsns.core;

/**
 * アプリで使用する定数定義クラス。
 */
public class AppConst {

	/** セッションキー：ログイン時のセッション情報。 */
	public static final String SESSION_KEY_LOGIN_INFO = "SESSION_KEY_LOGIN_INFO";

	/** フレンド 承認ステータス：1.申請中[自分] */
	public static final int FRIENDS_APPROVAL_STATUS_IN_PROGRESS = 1;
	/** フレンド 承認ステータス：2.承認待ち[相手] */
	public static final int FRIENDS_APPROVAL_STATUS_REVIEW = 2;
	/** フレンド 承認ステータス：3.承認[自分] */
	public static final int FRIENDS_APPROVAL_STATUS_GRANTED = 3;
	/** フレンド 承認ステータス：4.承諾[相手] */
	public static final int FRIENDS_APPROVAL_STATUS_CONSENT = 4;
}
