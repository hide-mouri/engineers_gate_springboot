package jp.dcworks.engineersgate.egsns.core;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jp.dcworks.engineersgate.egsns.core.annotation.LoginCheck;
import jp.dcworks.engineersgate.egsns.entity.Users;
import lombok.extern.log4j.Log4j2;

/**
 * アプリケーション中継処理。
 * ※クラス、メソッドどちらの設定でも呼び出し可能なよう実装する。
 *
 * @see 参考サイト：<a href="https://b1san-blog.com/post/spring/spring-interceptor/">【Spring Boot】Interceptorによる共通処理</a>
 * @author tomo-sato
 */
@Log4j2
@Component
public class AppIntercepter<A> implements HandlerInterceptor {

	/** セッション情報。 */
	@Autowired
	private HttpSession session;

	/** 処理開始時間を保持。 */
	private Date startTime;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 静的リソースの場合は処理不要
		if (handler instanceof ResourceHttpRequestHandler) {
			return true;
		}

		String requestUri = request.getRequestURI();
		String controllerUri = getControllerUri(requestUri);
		String method = request.getMethod();
		Map<String, String[]> requestParameterMap = request.getParameterMap();

		this.startTime = new Date();
		// ログにSessionIDを表示させるためセット。
		MDC.put("sessionId", session.getId());

		log.info("▼▼▼ [アプリケーション中継]コントローラ実行前処理（共通） ▼▼▼：{}\nmethod={}, params={}", requestUri, method,
				toStringParameterMap(requestParameterMap));

		// 以下は、特定のコントローラークラス、メソッドに実行したい場合の処理。
		if (isLoginCheckExists(handler)) {
			// LoginCheckアノテーションがついている、特定のコントローラーでの処理を行う。
			log.info("▼▼▼ [アプリケーション中継]コントローラ実行前処理（LoginCheckアノテーションがついている処理限定） ▼▼▼：{}", requestUri);

			Users users = (Users) session.getAttribute(AppConst.SESSION_KEY_LOGIN_INFO);
			if (users == null) {
				log.warn("ログイン情報が取得できませんでした。：controllerUri={}", controllerUri);
				throw new AppSessionTimeoutException("ログイン情報が取得できませんでした。：controllerUri=" + controllerUri);
			}
		}

		return true;
	}

	/**
	 * Controller名を取得する。
	 */
	private static String getControllerUri(String requestUri) {
		String[] uriItems = requestUri.split("/");

		String retUri = "";
		if (uriItems.length >= 2) {
			retUri = uriItems[1];
		}
		return retUri;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView model) throws Exception {

		// 静的リソースの場合は不要
		if (!(handler instanceof ResourceHttpRequestHandler)) {
			String requestUri = request.getRequestURI();
			Date elapsedTime = new Date();
			long elapsed = elapsedTime.getTime() - startTime.getTime();

			// コントローラーでの処理が完了し、ビューのレンダリング前に行いたい処理を記述する。
			log.info("▲▲▲ [アプリケーション中継]コントローラ処理完了、ビューのレンダリング前（共通） ▲▲▲：経過時間(" + elapsed + "ms)：" + requestUri);

			Users users = (Users) session.getAttribute(AppConst.SESSION_KEY_LOGIN_INFO);
			if (users != null && model != null) {
				// ログイン情報をセット。
				model.addObject("users", users);
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception exception) throws Exception {

		// 静的リソースの場合は不要
		if (!(handler instanceof ResourceHttpRequestHandler)) {

			Collection<String> headernames = response.getHeaderNames();
			for (String str : headernames) {
				log.info(str + ":" + response.getHeader(str));
			}

			String requestUri = request.getRequestURI();
			Date elapsedTime = new Date();
			long elapsed = elapsedTime.getTime() - startTime.getTime();

			// ビューのレンダリング後に行いたい処理を記述する。
			log.info("▲▲▲ [アプリケーション中継]コントローラ処理完了、ビューのレンダリング後（共通） ▲▲▲：経過時間(" + elapsed + "ms)：" + requestUri);
		}
	}

	/**
	 * handlerに{@link jp.co.bookoff.loyalty.mgr.intercepter.annotation.CheckReadPermission}アノテーションが付与されているか否か。
	 * クラスと、メソッドに対してチェック処理を行う。
	 *
	 * @param handler ハンドラ
	 * @return true.付与されている。false.付与されていない。
	 */
	private static boolean isLoginCheckExists(Object handler) {
		boolean isRet = false;

		if (handler == null) {
			log.info("handler=null");
			return isRet;
		}

		// ハンドラが対象のクラスだった場合、判定を行う。
		if (handler instanceof HandlerMethod) {
			isRet = ((AnnotationUtils.findAnnotation(((HandlerMethod) handler).getMethod(),
					LoginCheck.class) != null)
					|| (AnnotationUtils.findAnnotation(((HandlerMethod) handler).getBeanType(),
							LoginCheck.class) != null));
		} else {
			// その他のハンドラの場合、内容把握の為ログ出力。
			log.info(handler.getClass());
		}

		return isRet;
	}

	/**
	 * 引数のparameterMapから文字列を生成する。
	 *
	 * @param parameterMap 文字列生成対象のオブジェクト
	 * @return 引数で指定されたparameterMapオブジェクトの文字列。
	 */
	private static String toStringParameterMap(Map<String, String[]> parameterMap) {
		ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		String json = "";
		try {
			json = objectMapper.writeValueAsString(parameterMap);
		} catch (JsonProcessingException e) {
			log.warn("パラメータ解析中にエラーが発生しました。", e);
		}
		return json;
	}
}
