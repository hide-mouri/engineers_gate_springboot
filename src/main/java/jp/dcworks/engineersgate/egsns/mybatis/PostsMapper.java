package jp.dcworks.engineersgate.egsns.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.dcworks.engineersgate.egsns.mybatis.entity.PostsMappingEntity;

/**
 * 投稿マッパーインターフェース。
 */
@Mapper
public interface PostsMapper {

	/**
	 * 投稿テーブルより除外ユーザーID以外の投稿最新情報を取得する。
	 * 
	 * @param excludedUsersId 除外ユーザーID
	 * @return
	 */
	List<PostsMappingEntity> getNewPosts(@Param("excludedUsersId") Long excludedUsersId);
}
