package jp.dcworks.engineersgate.egsns.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import jp.dcworks.engineersgate.egsns.entity.Posts;

/**
 * 投稿関連リポジトリインターフェース。
 */
public interface PostsRepository extends PagingAndSortingRepository<Posts, Long>, CrudRepository<Posts, Long> {

	/**
	 * 投稿一覧を取得する。
	 * 投稿IDの降順。
	 * @return 投稿一覧を返す。
	 */
	List<Posts> findByOrderByIdDesc();

	/**
	 * 投稿一覧を取得する。
	 * 投稿IDの降順。
	 * @param usersId ユーザーID
	 * @return 投稿一覧を返す。
	 */
	List<Posts> findByUsersIdOrderByIdDesc(Long usersId);
}
