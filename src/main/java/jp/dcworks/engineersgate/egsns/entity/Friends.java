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
 * フレンドEntityクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "friends")
public class Friends extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ユーザーID */
	@Column(name = "users_id", nullable = false)
	private Long usersId;

	/** フレンドユーザーID */
	@Column(name = "friend_users_id", nullable = false)
	private Long friendUsersId;

	/** 承認ステータス（1.申請中[自分]、2.承認待ち[相手]、3.承認[自分]、4.承諾[相手]） */
	@Column(name = "approval_status", nullable = false)
	private Integer approvalStatus;

}
