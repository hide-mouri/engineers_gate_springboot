package jp.dcworks.engineersgate.egsns.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Collection操作に関する機能を提供します。
 */
@Component
public class CollectionUtil {

	/**
	 * Collectionがnullまたはsize=0か、否かをチェックします。
	 *
	 * @param list チェック対象のリスト
	 * @return 判定結果。true.empty、false.not empty
	 */
	public static boolean isEmpty(Collection<?> list) {
		if (list == null || list.size() <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * Mapがnullまたはsize=0か、否かをチェックします。
	 *
	 * @param map チェック対象のリスト
	 * @return 判定結果。true.empty、false.not empty
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.size() <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * {@link #isEmpty(Collection)} の反転。
	 */
	public static boolean isNotEmpty(Collection<?> list) {
		return !isEmpty(list);
	}

	/**
	 * {@link #isEmpty(Map)} の反転。
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}
}
