package jp.dcworks.engineersgate.egsns.mybatis.entity;

import java.util.Date;

import lombok.Data;

/**
 * MyBatis課題進捗率Entityクラス。
 */
@Data
public class PostsMappingEntity {

	/** ID */
	private Long id;

	/** ユーザーID */
	private Long usersId;

	/** タイトル */
	private String title;

	/** 本文 */
	private String body;

	/** 作成日時 */
	private Date created;

	/** 投稿画像URI */
	private String imageUri;

	/** 名前 */
	private String name;

	/** プロフィールアイコンURI */
	private String iconUri;
}
