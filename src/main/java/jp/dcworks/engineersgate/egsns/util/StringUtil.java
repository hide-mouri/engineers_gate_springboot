package jp.dcworks.engineersgate.egsns.util;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.util.StringUtils;

/**
 * 文字列操作に関する機能を提供します。
 */
@Component
public class StringUtil {

	/** ブランク */
	public static final String BLANK = "";

	/**
	 * 引数のObjectに対する{@link #toString()}を返します。
	 * <p>
	 * ※{@link NullPointerException} 回避の為のユーティリティ。
	 * </p>
	 *
	 * @param obj toStringしたいオブジェクト
	 * @return toStringした結果。引数がnullの場合、nullを返します。
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	/**
	 * 引数のObjectに対する{@link #toString()}を返します。
	 * <p>
	 * ※結果がnullの場合、{@code defaultValue} を返します。
	 * </p>
	 *
	 * @param obj toStringしたいオブジェクト
	 * @param defaultValue 結果がnullだった場合のデフォルト値
	 * @return toStringした結果。引数がnullの場合、defaultValueを返します。
	 */
	public static String toString(Object obj, String defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		return obj.toString();
	}

	/**
	 * 引数のObjectに対する{@link #toString()}を返します。
	 * <p>
	 * ※結果がnullの場合、ブランクを返します。
	 * </p>
	 *
	 * @param obj toStringしたいオブジェクト
	 * @return toStringした結果。引数がnullの場合、ブランクを返します。
	 */
	public static String toStringOrBlank(final Object obj) {
		return Objects.toString(obj, BLANK);
	}

	/**
	 * 改行コードを<br />タグに変換した情報を返却する。
	 * @param s 入力文字列
	 * @return 変換後の文字列を返却します。
	 */
	public static String nl2br(String s) {
		return nl2br(s, true);
	}

	/**
	 * 改行コードを<br />、または、<br>タグに変換した情報を返却する。
	 * @param str 入力文字列
	 * @param isXhtml XHTML準拠の改行タグの使用する場合はtrueを指定します。
	 * @return 変換後の文字列を返却します。
	 */
	public static String nl2br(String str, boolean isXhtml) {
		if (str == null || "".equals(str)) {
			return "";
		}

		// タグ文字が含まれる場合はエスケープする。
		String escapeStr = HtmlUtils.htmlEscape(str);

		String tag = isXhtml ? "<br />" : "<br>";
		return escapeStr.replaceAll("\\r\\n|\\n\\r|\\n|\r", tag);
	}

	/**
	 * 改行コードを<br />、または、<br>タグに変換した情報を返却する。
	 * @param str 入力文字列
	 * @param maxWidth 表示時の最大文字数。（maxWidthには"..."も含まれるため4以上にして下さい。4未満の場合、4を設定した結果を返します。）
	 * @return 変換後の文字列を返却します。
	 */
	public static String nl2br(String str, int maxWidth) {
		if (str == null || "".equals(str)) {
			return "";
		}

		String tmpStr = StringUtils.abbreviate(str, maxWidth);

		// タグ文字が含まれる場合はエスケープする。
		return nl2br(tmpStr, true);
	}

	/**
	 * 省略表示を行います。
	 *
	 * @param str 省略したい文字
	 * @param maxWidth 表示時の最大文字数。（maxWidthには"..."も含まれるため4以上にして下さい。4未満の場合、4を設定した結果を返します。）
	 * @return 省略された文字列を返却します。
	 */
	public static String abbreviate(String str, int maxWidth) {
		if (maxWidth < 4) {
			maxWidth = 4;
		}
		return StringUtils.abbreviate(str, maxWidth);
	}

}
