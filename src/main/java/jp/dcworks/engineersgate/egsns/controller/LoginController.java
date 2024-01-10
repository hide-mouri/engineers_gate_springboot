package jp.dcworks.engineersgate.egsns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jp.dcworks.engineersgate.egsns.core.AppConst;
import jp.dcworks.engineersgate.egsns.dto.RequestLogin;
import jp.dcworks.engineersgate.egsns.entity.Users;
import jp.dcworks.engineersgate.egsns.service.UsersService;
import jp.dcworks.engineersgate.egsns.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@Log4j2
@Controller
@RequestMapping("/")
public class LoginController {

	/** セッション情報。 */
	@Autowired
	private HttpSession session;

	/** ユーザー関連サービスクラス。 */
	@Autowired
	private UsersService usersService;

	/**
	 * [GET]ログインフォームのアクション。
	 *
	 * @param model 画面にデータを送るためのオブジェクト
	 */
	@GetMapping("")
	public String index(Model model) {

		log.info("ログイン画面のアクションが呼ばれました。");

		if (!model.containsAttribute("requestLogin")) {
			model.addAttribute("requestLogin", new RequestLogin());
		}

		// ログイン済みの場合、ホームへリダイレクト
		Users users = (Users) session.getAttribute(AppConst.SESSION_KEY_LOGIN_INFO);
		if (users != null) {

			// ホーム画面へリダイレクト。
			return "redirect:/home";
		}

		return "login/index";
	}

	/**
	 * [POST]ログインアクション。
	 *
	 * @param requestAccount 入力フォームの内容
	 * @param result バリデーション結果
	 * @param redirectAttributes リダイレクト時に使用するオブジェクト
	 */
	@PostMapping("login")
	public String login(@Validated @ModelAttribute RequestLogin requestLogin,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		log.info("ログイン処理のアクションが呼ばれました。");

		// バリデーション。
		if (result.hasErrors()) {
			// javascriptのバリデーションを改ざんしてリクエストした場合に通る処理。
			log.warn("バリデーションエラーが発生しました。：requestLogin={}, result={}", requestLogin, result);

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestLogin", requestLogin);

			// ログイン画面へリダイレクト。
			return "redirect:/";
		}

		// ログインIDとパスワードを取得。
		String loginId = requestLogin.getLoginId();
		String password = requestLogin.getPassword();

		// ユーザー検索を行う。
		Users users = usersService.findUsers(loginId, password);

		// ユーザーが取得できなかったら、ログインエラー。
		if (users == null) {
			log.warn("ログインに失敗しました。：requestLogin={}", requestLogin);

			// エラーメッセージをセット。
			result.rejectValue("loginId", StringUtil.BLANK, "ログインに失敗しました。入力内容をご確認の上、再度ログインしてください。");

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestLogin", requestLogin);

			// ログイン画面へリダイレクト。
			return "redirect:/";
		}

		// ログインに成功したら、ログイン情報をセッションに保持。
		session.setAttribute(AppConst.SESSION_KEY_LOGIN_INFO, users);

		// ホーム画面へリダイレクト。
		return "redirect:/home";
	}

	/**
	 * [GET]ログアウトアクション。
	 */
	@GetMapping("logout")
	public String logout() {

		// セッション情報をクリア
		session.removeAttribute(AppConst.SESSION_KEY_LOGIN_INFO);

		// ログイン画面へリダイレクト。
		return "redirect:/";
	}
}
