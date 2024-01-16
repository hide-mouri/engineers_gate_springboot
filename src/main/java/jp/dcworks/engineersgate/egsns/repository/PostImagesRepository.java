package jp.dcworks.engineersgate.egsns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import jp.dcworks.engineersgate.egsns.entity.PostImages;

/**
 * 投稿画像関連リポジトリインターフェース。
 */
public interface PostImagesRepository
		extends PagingAndSortingRepository<PostImages, Long>, CrudRepository<PostImages, Long> {
}
