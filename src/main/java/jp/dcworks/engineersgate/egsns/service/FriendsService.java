package jp.dcworks.engineersgate.egsns.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.engineersgate.egsns.core.AppConst;
import jp.dcworks.engineersgate.egsns.entity.Friends;
import jp.dcworks.engineersgate.egsns.repository.FriendsRepository;
import lombok.extern.log4j.Log4j2;

/**
 * フレンド関連サービスクラス。
 */
@Log4j2
@Service
public class FriendsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private FriendsRepository repository;

	/**
	 * フレンド検索を行う。
	 *
	 * @param loginUsersId ログインユーザーID
	 * @param friendsUsersId フレンドユーザーID
	 * @return フレンド情報を返す。
	 */
	public Friends findByUsersIdAndFriendUsersId(Long loginUsersId, Long friendsUsersId) {
		log.info("フレンドを検索します。：loginUsersId={}, friendsUsersId={}", loginUsersId, friendsUsersId);

		Friends friends = repository.findFirstByUsersIdAndFriendUsersId(loginUsersId, friendsUsersId);
		log.info("フレンド検索結果。：loginUsersId={}, friendsUsersId={}, friends={}", loginUsersId, friendsUsersId, friends);

		return friends;
	}

	/**
	 * 申請情報の登録処理を行う。
	 *
	 * @param fromUsersId 申請を送った人のユーザーID
	 * @param toUsersId 申請先のユーザーID
	 */
	public void saveFriendRequest(Long fromUsersId, Long toUsersId) {
		Friends fromFriends = new Friends();
		fromFriends.setUsersId(fromUsersId);
		fromFriends.setFriendUsersId(toUsersId);
		// 1.申請中[自分] をセット
		fromFriends.setApprovalStatus(AppConst.FRIENDS_APPROVAL_STATUS_IN_PROGRESS);

		Friends toFriends = new Friends();
		toFriends.setUsersId(toUsersId);
		toFriends.setFriendUsersId(fromUsersId);
		// 2.承認待ち[相手] をセット
		toFriends.setApprovalStatus(AppConst.FRIENDS_APPROVAL_STATUS_REVIEW);

		List<Friends> saveList = new ArrayList<Friends>();
		saveList.add(fromFriends);
		saveList.add(toFriends);

		repository.saveAll(saveList);
	}

	/**
	 * 承認情報の登録処理を行う。
	 *
	 * @param fromUsersId 申請をもらった人のユーザーID（※つまりログインユーザー）
	 * @param toUsersId 申請元のユーザーID（※つまり相手）
	 */
	public void saveApproveFriendRequest(Long fromUsersId, Long toUsersId) {

		// 自身のフレンド情報を取得する。（2.承認待ち　→　4.承諾）
		Friends fromFriends = repository.findFirstByUsersIdAndFriendUsersId(fromUsersId, toUsersId);
		fromFriends.setApprovalStatus(AppConst.FRIENDS_APPROVAL_STATUS_CONSENT);

		// 相手のフレンド情報を取得する。（1.申請中　→　3.承認）
		Friends toFriends = repository.findFirstByUsersIdAndFriendUsersId(toUsersId, fromUsersId);
		toFriends.setApprovalStatus(AppConst.FRIENDS_APPROVAL_STATUS_GRANTED);

		List<Friends> saveList = new ArrayList<Friends>();
		saveList.add(fromFriends);
		saveList.add(toFriends);

		repository.saveAll(saveList);
	}

	/**
	 * 削除処理を行う。
	 *
	 * @param fromUsersId 申請をもらった人のユーザーID（※つまりログインユーザー）
	 * @param toUsersId 申請元のユーザーID（※つまり相手）
	 */
	public void deleteFriendRequest(Long fromUsersId, Long toUsersId) {
		// 自身のフレンド情報を取得する。
		Friends fromFriends = repository.findFirstByUsersIdAndFriendUsersId(fromUsersId, toUsersId);

		// 相手のフレンド情報を取得する。
		Friends toFriends = repository.findFirstByUsersIdAndFriendUsersId(toUsersId, fromUsersId);

		List<Friends> saveList = new ArrayList<Friends>();
		saveList.add(fromFriends);
		saveList.add(toFriends);

		repository.deleteAll(saveList);
	}

	/**
	 * フレンド検索を行う。
	 *
	 * @param loginUsersId ログインユーザーID
	 * @return フレンド一覧を返す。
	 */
	public List<Friends> findByFriendsList(Long loginUsersId) {
		log.info("フレンドを検索します。：loginUsersId={}", loginUsersId);

		List<Friends> friendsList = repository.findByUsersId(loginUsersId);
		log.info("フレンド検索結果。：loginUsersId={}, friends={}", loginUsersId, friendsList);

		return friendsList;
	}
}
