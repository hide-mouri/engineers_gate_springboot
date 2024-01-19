package jp.dcworks.engineersgate.egsns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.dcworks.engineersgate.egsns.core.annotation.LoginCheck;
import jp.dcworks.engineersgate.egsns.entity.Users;
import jp.dcworks.engineersgate.egsns.service.FriendsService;
import jp.dcworks.engineersgate.egsns.service.UsersService;
import lombok.extern.log4j.Log4j2;

/**
 * ユーザー画面コントローラー。
 */
@LoginCheck
@Log4j2
@Controller
@RequestMapping("/user")
public class UserController extends AppController {

	/** ユーザー関連サービスクラス。 */
	@Autowired
	private UsersService usersService;

	/** フレンド関連サービスクラス。 */
	@Autowired
	private FriendsService friendsService;

	/**
	 * [GET]フレンド一覧画面のアクション。
	 *
	 * @param model 入力フォームのオブジェクト
	 */
	@GetMapping("/list")
	public String list(Model model) {
		log.info("ユーザー一覧画面のアクションが呼ばれました。");

		// ログインユーザーを取得。
		Long loginUsersId = getUsersId();

		// フレンド情報を取得する。
		Iterable<Users> usersList = usersService.findAll();
		model.addAttribute("usersList", usersList);

		return "user/list";
	}
}
