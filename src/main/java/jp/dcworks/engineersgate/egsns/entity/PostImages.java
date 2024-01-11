package jp.dcworks.engineersgate.egsns.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投稿画像Entityクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post_images")
public class PostImages extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 投稿ID */
	@Column(name = "posts_id", nullable = false)
	private Long postsId;

	/** ユーザーID */
	@Column(name = "users_id", nullable = false)
	private Long usersId;

	/** 投稿画像URI */
	@Column(name = "image_uri", nullable = false)
	private String imageUri;
}
