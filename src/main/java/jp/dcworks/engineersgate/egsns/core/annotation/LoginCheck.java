package jp.dcworks.engineersgate.egsns.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ログインチェックを行う場合に使用するマーカーアノテーション。
 * ※クラス、メソッドどちらでも設定可能。
 *
 * @author tomo-sato
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface LoginCheck {
}
