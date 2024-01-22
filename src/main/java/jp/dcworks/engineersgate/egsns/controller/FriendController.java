package jp.dcworks.engineersgate.egsns.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.dcworks.engineersgate.egsns.core.AppConst;
import jp.dcworks.engineersgate.egsns.core.annotation.LoginCheck;
import jp.dcworks.engineersgate.egsns.entity.Friends;
import jp.dcworks.engineersgate.egsns.entity.Users;
import jp.dcworks.engineersgate.egsns.service.FriendsService;
import jp.dcworks.engineersgate.egsns.service.UsersService;
import jp.dcworks.engineersgate.egsns.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * フレンド画面コントローラー。
 */
@LoginCheck
@Log4j2
@Controller
@RequestMapping("/friend")
public class FriendController extends AppController {

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
		log.info("フレンド一覧画面のアクションが呼ばれました。");

		// ログインユーザーを取得。
		Long loginUsersId = getUsersId();

		// フレンド情報を取得する。
		List<Friends> friendsList = friendsService.findByFriendsList(loginUsersId);
		model.addAttribute("friendsList", friendsList);

		// ユーザー情報を取得する。
		Iterable<Users> usersIterableList = usersService.findAll();
		List<Users> usersList = new ArrayList<Users>();

		// 自身のフレンド情報を取得する。
		List<Friends> tmpFriendsList = null;
		for (Users users : usersIterableList) {
			if (loginUsersId.equals(users.getId().longValue())) {
				tmpFriendsList = users.getFriendsList();

			} else {
				// 自分を除外してリスト作成。
				usersList.add(users);
			}
		}

		// 承認ステータス情報の付与。
		for (Friends friends : tmpFriendsList) {
			Long friendUsersId = friends.getFriendUsersId();

			int size = usersList.size();
			for (int i = 0; i < size; i++) {
				Users users = usersList.get(i);
				if (friendUsersId.equals(users.getId())) {
					users.setFriendsInfo(friends);
					usersList.set(i, users);
					break;
				}
			}
		}

		model.addAttribute("usersList", usersList);

		return "friend/list";
	}

	/**
	 * [GET]友達申請を行うアクション。
	 *
	 * @param model 入力フォームのオブジェクト
	 * @param result バリデーション結果
	 * @param usersId ユーザーID
	 * @throws URISyntaxException 
	 */
	@GetMapping("/request/{usersId}")
	public String sendFriendRequest(
			@RequestHeader("Referer") String referer,
			Model model,
			@PathVariable("usersId") String usersId,
			RedirectAttributes redirectAttributes) throws URISyntaxException {

		BindingResult bindingResult = new BindException(model, "form");

		log.info("フレンド申請のアクションが呼ばれました。：usersId={}", usersId);

		// ログインユーザーを取得。
		Users loginUsers = getUsers();
		Long loginUsersId = loginUsers.getId();

		// 申請先が自分自身ではないことのチェック。
		if (loginUsersId.toString().equals(usersId)) {
			log.warn("申請先が不正です。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "申請先が不正です。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// プロフィール画面へリダイレクト。
			return "redirect:" + new URI(referer).getPath();
		}

		// 申請先ユーザーの存在チェック。
		Users profileUsers = usersService.findById(usersId);
		if (profileUsers == null) {
			log.warn("申請対象のユーザーが見つかりませんでした。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "申請対象のユーザーが見つかりませんでした。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// プロフィール画面へリダイレクト。
			return "redirect:" + new URI(referer).getPath();
		}
		Long profileUsersId = profileUsers.getId();

		// 申請先のユーザーのフレンド情報を検索。
		Friends friendsInfo = friendsService.findByUsersIdAndFriendUsersId(loginUsersId, profileUsersId);

		// フレンド情報を検索、申請済みではないことの確認。
		if (friendsInfo != null) {
			log.warn("すでに申請済みです。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "すでに申請済みです。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// プロフィール画面へリダイレクト。
			return "redirect:" + new URI(referer).getPath();
		}

		// ログインユーザー向けに、申請中のレコードを作成。
		//　申請先のユーザー向けに、承認待ちのレコードを作成。
		friendsService.saveFriendRequest(loginUsersId, profileUsersId);

		return "redirect:" + new URI(referer).getPath();
	}

	/**
	 * [GET]友達申請承認を行うアクション。
	 *
	 * @param model 入力フォームのオブジェクト
	 * @param result バリデーション結果
	 * @param usersId ユーザーID
	 */
	@GetMapping("/request/approve/{usersId}")
	public String approveFriendRequest(Model model, @PathVariable("usersId") String usersId,
			RedirectAttributes redirectAttributes) {

		BindingResult bindingResult = new BindException(model, "form");

		log.info("フレンド承認のアクションが呼ばれました。：usersId={}", usersId);

		// ログインユーザーを取得。
		Users loginUsers = getUsers();
		Long loginUsersId = loginUsers.getId();

		// 承認が自分自身ではないことのチェック。
		if (loginUsersId.toString().equals(usersId)) {
			log.warn("承認処理が不正です。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "承認処理が不正です。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// フレンド画面へリダイレクト。
			return "redirect:/friend/list";
		}

		// 承認対象のユーザー存在チェック。
		Users profileUsers = usersService.findById(usersId);
		if (profileUsers == null) {
			log.warn("承認対象のユーザーが見つかりませんでした。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "承認対象のユーザーが見つかりませんでした。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// フレンド画面へリダイレクト。
			return "redirect:/friend/list";
		}
		Long profileUsersId = profileUsers.getId();

		// 承認対象のユーザーのフレンド情報を検索。
		Friends friendsInfo = friendsService.findByUsersIdAndFriendUsersId(loginUsersId, profileUsersId);

		// フレンド情報を検索、「2.承認待ち」であることの確認。（※つまり、友達申請を受け取っているかどうかのチェック。）
		if (friendsInfo == null
				|| AppConst.FRIENDS_APPROVAL_STATUS_REVIEW != friendsInfo.getApprovalStatus().intValue()) {

			log.warn("承認対象のユーザーは、承認待ちステータスではありません。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "承認対象のユーザーは、承認待ちステータスではありません。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// フレンド画面へリダイレクト。
			return "redirect:/friend/list";
		}

		// 下記更新を行う。
		// 　自分：2.承認待ち　→　4.承諾
		// 　相手：1.申請中　→　3.承認
		friendsService.saveApproveFriendRequest(loginUsersId, profileUsersId);

		return "redirect:/friend/list";
	}

	/**
	 * [GET]友達申請キャンセルを行うアクション。
	 *
	 * @param model 入力フォームのオブジェクト
	 * @param result バリデーション結果
	 * @param usersId ユーザーID
	 */
	@GetMapping("/request/cancel/{usersId}")
	public String cancelFriendRequest(Model model, @PathVariable("usersId") String usersId,
			RedirectAttributes redirectAttributes) {

		BindingResult bindingResult = new BindException(model, "form");

		log.info("フレンドキャンセルのアクションが呼ばれました。：usersId={}", usersId);

		// ログインユーザーを取得。
		Users loginUsers = getUsers();
		Long loginUsersId = loginUsers.getId();

		// キャンセル先が自分自身ではないことのチェック。
		if (loginUsersId.toString().equals(usersId)) {
			log.warn("キャンセル処理が不正です。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "キャンセル処理が不正です。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// フレンド画面へリダイレクト。
			return "redirect:/friend/list";
		}

		// キャンセル対象のユーザー存在チェック。
		Users profileUsers = usersService.findById(usersId);
		if (profileUsers == null) {
			log.warn("キャンセル対象のユーザーが見つかりませんでした。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "キャンセル対象のユーザーが見つかりませんでした。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// フレンド画面へリダイレクト。
			return "redirect:/friend/list";
		}
		Long profileUsersId = profileUsers.getId();

		// 承認対象のユーザーのフレンド情報を検索。
		Friends friendsInfo = friendsService.findByUsersIdAndFriendUsersId(loginUsersId, profileUsersId);

		// フレンド情報を検索（※今回、処理簡略により物理削除する為、存在していればOK。）
		if (friendsInfo == null) {

			log.warn("キャンセル対象のユーザーが見つかりませんでした。：usersId={}, loginUsers={}", usersId, loginUsers);

			// エラーメッセージをセット。
			bindingResult.rejectValue("", StringUtil.BLANK, "キャンセル対象のユーザーが見つかりませんでした。");
			redirectAttributes.addFlashAttribute("validationErrors", bindingResult);

			// フレンド画面へリダイレクト。
			return "redirect:/friend/list";
		}

		// 削除処理を行う。
		friendsService.deleteFriendRequest(loginUsersId, profileUsersId);

		return "redirect:/friend/list";
	}
}
