package jp.dcworks.engineersgate.egsns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.engineersgate.egsns.dto.RequestShare;
import jp.dcworks.engineersgate.egsns.entity.PostImages;
import jp.dcworks.engineersgate.egsns.entity.Posts;
import jp.dcworks.engineersgate.egsns.repository.PostsImagesRepository;
import jp.dcworks.engineersgate.egsns.repository.PostsRepository;
import lombok.extern.log4j.Log4j2;

/**
 * 投稿関連サービスクラス。
 */
@Log4j2
@Service
public class PostsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private PostsRepository repository;

	/** 投稿関連リポジトリインターフェース。 */
	@Autowired
	private PostsImagesRepository postsImagesRepository;

	/**
	 * 投稿処理を行う。
	 *
	 * @param requestShare コメント投稿DTO
	 * @param usersId ユーザーID
	 * @param postImagesFileUri 投稿画像URI
	 */
	public void save(RequestShare requestShare, Long usersId, String postImagesFileUri) {
		log.info("投稿処理を行います。：requestShare={}, usersId={}, postImagesFileUri={}", requestShare, usersId,
				postImagesFileUri);

		Posts posts = new Posts();
		posts.setUsersId(usersId);
		posts.setTitle(requestShare.getTitle());
		posts.setBody(requestShare.getBody());

		// 投稿データの登録及び、取得。
		Posts regPosts = repository.save(posts);
		Long postsId = regPosts.getId();

		if (postImagesFileUri != null) {
			PostImages postImages = new PostImages();
			postImages.setPostsId(postsId);
			postImages.setUsersId(usersId);
			postImages.setImageUri(postImagesFileUri);
			postsImagesRepository.save(postImages);
		}
	}

	/**
	 * 投稿一覧を取得する。
	 * 投稿IDの降順。
	 * @return 投稿一覧を返す。
	 */
	public List<Posts> findAllPosts() {
		return (List<Posts>) repository.findByOrderByIdDesc();
	}

	/**
	 * 投稿一覧を取得する。
	 * 投稿IDの降順。
	 * @param usersId ユーザーID
	 * @return 投稿一覧を返す。
	 */
	public List<Posts> findByUsersId(Long usersId) {
		return (List<Posts>) repository.findByUsersIdOrderByIdDesc(usersId);
	}
}
