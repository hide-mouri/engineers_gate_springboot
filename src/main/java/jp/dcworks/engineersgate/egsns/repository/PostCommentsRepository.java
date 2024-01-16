package jp.dcworks.engineersgate.egsns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import jp.dcworks.engineersgate.egsns.entity.PostComments;

/**
 * 投稿コメント関連リポジトリインターフェース。
 */
public interface PostCommentsRepository
		extends PagingAndSortingRepository<PostComments, Long>, CrudRepository<PostComments, Long> {
}
