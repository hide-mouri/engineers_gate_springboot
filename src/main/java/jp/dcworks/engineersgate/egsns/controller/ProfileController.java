package jp.dcworks.engineersgate.egsns.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.dcworks.engineersgate.egsns.core.annotation.LoginCheck;
import jp.dcworks.engineersgate.egsns.dto.RequestModifyAccount;
import jp.dcworks.engineersgate.egsns.entity.Friends;
import jp.dcworks.engineersgate.egsns.entity.Users;
import jp.dcworks.engineersgate.egsns.service.FriendsService;
import jp.dcworks.engineersgate.egsns.service.StorageService;
import jp.dcworks.engineersgate.egsns.service.UsersService;
import jp.dcworks.engineersgate.egsns.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@LoginCheck
@Log4j2
@Controller
@RequestMapping("/profile")
public class ProfileController extends AppController {

	/** ファイルアップロード関連サービスクラス。 */
	@Autowired
	private StorageService storageService;

	/** ユーザー関連サービスクラス。 */
	@Autowired
	private UsersService usersService;

	/** フレンド関連サービスクラス。 */
	@Autowired
	private FriendsService friendsService;

	/**
	 * [GET]プロフィール画面のアクション。
	 *
	 * @param model 入力フォームのオブジェクト
	 * @param usersId ユーザーID
	 */
	@GetMapping(path = { "", "/", "/{usersId}" })
	public String index(Model model, @PathVariable(name = "usersId", required = false) String usersId) {
		log.info("プロフィール画面のアクションが呼ばれました。：usersId={}", usersId);

		// ログインユーザーを取得、画面にセット。
		Users loginUsers = getUsers();
		model.addAttribute("loginUsers", loginUsers);

		// ログインユーザーと異なる場合、プロフィールに表示するユーザー情報を検索。
		Users profileUsers = loginUsers;
		if (usersId != null && !loginUsers.getId().toString().equals(usersId)) {
			profileUsers = usersService.findById(usersId);
		}
		model.addAttribute("profileUsers", profileUsers);

		// 申請情報を検索。
		Friends friends = friendsService.findByUsersIdAndFriendUsersId(loginUsers.getId(), profileUsers.getId());
		model.addAttribute("friends", friends);

		return "profile/index";
	}

	/**
	 * [POST]アカウント編集アクション。
	 *
	 * @param requestModifyAccount 入力フォームの内容
	 * @param profileFile プロフィール画像ファイル
	 * @param result バリデーション結果
	 * @param redirectAttributes リダイレクト時に使用するオブジェクト
	 */
	@PostMapping("/regist")
	public String regist(@Validated @ModelAttribute RequestModifyAccount requestModifyAccount,
			@RequestParam("profileFile") MultipartFile profileFile,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		log.info("プロフィール編集処理のアクションが呼ばれました。");

		// バリデーション。
		if (result.hasErrors()) {
			// javascriptのバリデーションを改ざんしてリクエストした場合に通る処理。
			log.warn("バリデーションエラーが発生しました。：requestModifyAccount={}, result={}", requestModifyAccount, result);

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestModifyAccount", requestModifyAccount);

			// 入力画面へリダイレクト。
			return "redirect:/profile";
		}

		// ファイルチェックを行う。
		if (!StorageService.isImageFile(profileFile)) {
			log.warn("指定されたファイルは、画像ファイルではありません。：requestModifyAccount={}", requestModifyAccount);

			// エラーメッセージをセット。
			result.rejectValue("profileFileHidden", StringUtil.BLANK, "画像ファイルを指定してください。");

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestModifyAccount", requestModifyAccount);

			// 入力画面へリダイレクト。
			return "redirect:/profile";
		}

		// ユーザー検索を行う。
		Users users = getUsers();

		// ファイルアップロード処理。
		String fileUri = storageService.store(profileFile);

		// fileUriが取得できない且つ、hiddenの値にファイルが設定されている場合は「設定済みのファイルが変更されていない状態」である為、hiddenの値で更新する。
		if (StringUtils.isEmpty(fileUri) && !StringUtils.isEmpty(requestModifyAccount.getProfileFileHidden())) {
			fileUri = requestModifyAccount.getProfileFileHidden();
			// DBから取得したデータと比較し、改ざんされた値ではないことの確認。
			if (!fileUri.equals(users.getIconUri())) {
				// 改ざんの可能性がある場合はnullをセットしファイルをクリアする。
				fileUri = null;
			}
		}

		users.setName(requestModifyAccount.getName());
		users.setEmailAddress(requestModifyAccount.getEmailAddress());
		users.setProfile(requestModifyAccount.getProfile());
		users.setIconUri(fileUri);
		usersService.save(users);

		try {
			// TODO 暫定：キャッシュの影響か、画像が完全にアップされる前に描画され、画像のリンク切れが発生している為、スリープして上がり切るまで待つ処理。
			Thread.sleep(3000);
		} catch (Exception e) {
		}

		return "redirect:/profile";
	}
}
