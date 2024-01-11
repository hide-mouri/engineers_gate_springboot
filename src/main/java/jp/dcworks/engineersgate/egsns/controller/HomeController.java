package jp.dcworks.engineersgate.egsns.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.dcworks.engineersgate.egsns.core.annotation.LoginCheck;
import jp.dcworks.engineersgate.egsns.dto.RequestPostCurrentFeelings;
import jp.dcworks.engineersgate.egsns.entity.Posts;
import jp.dcworks.engineersgate.egsns.service.PostsService;
import jp.dcworks.engineersgate.egsns.service.StorageService;
import jp.dcworks.engineersgate.egsns.util.StringUtil;
import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@LoginCheck
@Log4j2
@Controller
@RequestMapping("/home")
public class HomeController extends AppController {

	/** ファイルアップロード関連サービスクラス。 */
	@Autowired
	private StorageService storageService;

	/** ユーザー関連サービスクラス。 */
	@Autowired
	private PostsService postsService;

	/**
	 * [GET]ホーム画面のアクション。
	 *
	 * @return
	 */
	@GetMapping(path = { "", "/" })
	public String index(Model model) {
		log.info("ホーム画面のアクションが呼ばれました。");

		if (!model.containsAttribute("requestPostCurrentFeelings")) {
			model.addAttribute("requestPostCurrentFeelings", new RequestPostCurrentFeelings());
		}

		// 投稿一覧取得。
		List<Posts> postsList = postsService.findAllPosts();
		model.addAttribute("postsList", postsList);

		return "home/index";
	}

	/**
	 * [POST]自身のコメント投稿アクション。
	 *
	 * @param requestPostCurrentFeelings 入力フォームの内容
	 * @param result バリデーション結果
	 * @param redirectAttributes リダイレクト時に使用するオブジェクト
	 */
	@PostMapping("/post_current_feelings")
	public String postCurrentFeelings(@Validated @ModelAttribute RequestPostCurrentFeelings requestPostCurrentFeelings,
			@RequestParam("postImagesFile") MultipartFile postImagesFile,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		log.info("コメント投稿処理のアクションが呼ばれました。：requestTopicComment={}", requestPostCurrentFeelings);

		// バリデーション。
		if (result.hasErrors()) {
			log.warn("バリデーションエラーが発生しました。：requestTopicComment={}, result={}", requestPostCurrentFeelings, result);

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestPostCurrentFeelings", requestPostCurrentFeelings);

			// 入力画面へリダイレクト。
			return "redirect:/home";
		}

		// ファイルチェックを行う。
		if (!StorageService.isImageFile(postImagesFile)) {
			log.warn("指定されたファイルは、画像ファイルではありません。：requestTopicComment={}", requestPostCurrentFeelings);

			// エラーメッセージをセット。
			result.rejectValue("profileFileHidden", StringUtil.BLANK, "画像ファイルを指定してください。");

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestPostCurrentFeelings", requestPostCurrentFeelings);

			// 入力画面へリダイレクト。
			return "redirect:/profile";
		}

		// ログインユーザー情報取得。
		Long usersId = getUsersId();

		// ファイルアップロード処理。
		String postImagesFileUri = storageService.store(postImagesFile);

		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}

		// コメント登録処理。
		postsService.save(requestPostCurrentFeelings, usersId, postImagesFileUri);

		return "redirect:/home";
	}
}
