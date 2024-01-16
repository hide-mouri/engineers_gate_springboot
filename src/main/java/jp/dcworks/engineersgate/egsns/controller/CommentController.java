package jp.dcworks.engineersgate.egsns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.dcworks.engineersgate.egsns.core.annotation.LoginCheck;
import jp.dcworks.engineersgate.egsns.dto.RequestComment;
import jp.dcworks.engineersgate.egsns.service.PostCommentsService;
import lombok.extern.log4j.Log4j2;

/**
 * ※TODO 適宜実装を入れてください。
 */
@LoginCheck
@Log4j2
@Controller
@RequestMapping("/comment")
public class CommentController extends AppController {

	/** 投稿コメント関連サービスクラス。 */
	@Autowired
	private PostCommentsService postCommentsService;

	/**
	 * [POST]記事コメント投稿アクション。
	 *
	 * @param requestShare 入力フォームの内容
	 * @param result バリデーション結果
	 * @param redirectAttributes リダイレクト時に使用するオブジェクト
	 */
	@PostMapping("/{postsId}")
	public String indes(@PathVariable("postsId") String postsId,
			@Validated @ModelAttribute RequestComment requestComment,
			BindingResult result,
			RedirectAttributes redirectAttributes) {

		log.info("コメント投稿処理のアクションが呼ばれました。：requestComment={}", requestComment);

		// バリデーション。
		if (result.hasErrors()) {
			log.warn("バリデーションエラーが発生しました。：requestComment={}, result={}", requestComment, result);

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestComment", requestComment);

			// 入力画面へリダイレクト。
			return "redirect:/home";
		}

		// TODO バリデーション（投稿ID）
		Long lPostsId = Long.parseLong(postsId);

		// ログインユーザー情報取得。
		Long usersId = getUsersId();

		// コメント登録処理。
		postCommentsService.save(requestComment, lPostsId, usersId);

		return "redirect:/home";
	}
}
