package jp.dcworks.engineersgate.egsns.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Component;

/**
 * 日付操作に関する機能を提供します。
 */
@Component
public class DateUtil {

	/**
	 * 「◯日前」等の表記に変換する。
	 * 
	 * @param targetDate 対象の日時
	 * @return 変換後の文字列。
	 */
	public static String prettyTimeFormat(Date targetDate) {
		PrettyTime prettyTime = new PrettyTime();
		return prettyTime.format(targetDate);
	}

	/**
	 * 日時のフォーマット変換を行う。
	 * 
	 * @param targetDate 対象の日時
	 * @return 変換後の文字列。
	 */
	public static String format(Date targetDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(targetDate);
	}
}
