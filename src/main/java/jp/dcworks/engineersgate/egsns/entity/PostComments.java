package jp.dcworks.engineersgate.egsns.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投稿コメントEntityクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post_comments")
public class PostComments extends EntityBase {

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

	/** コメント */
	@Column(name = "comment", nullable = false)
	private String comment;

	/** ユーザー情報の紐づけ */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users users;
}
