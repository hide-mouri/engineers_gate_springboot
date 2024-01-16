package jp.dcworks.engineersgate.egsns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.engineersgate.egsns.dto.RequestComment;
import jp.dcworks.engineersgate.egsns.entity.PostComments;
import jp.dcworks.engineersgate.egsns.repository.PostCommentsRepository;
import lombok.extern.log4j.Log4j2;

/**
 * 投稿コメント関連サービスクラス。
 */
@Log4j2
@Service
public class PostCommentsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private PostCommentsRepository repository;

	/**
	 * コメント投稿処理を行う。
	 *
	 * @param requestComment コメント投稿DTO
	 * @param postsId 投稿ID
	 * @param usersId ユーザーID
	 */
	public void save(RequestComment requestComment, Long postsId, Long usersId) {
		log.info("投稿処理を行います。：requestComment={}, usersId={}", requestComment, usersId);

		PostComments postComments = new PostComments();
		postComments.setPostsId(postsId);
		postComments.setUsersId(usersId);
		postComments.setComment(requestComment.getComment());

		// 投稿データの登録及び、取得。
		repository.save(postComments);
	}
}
