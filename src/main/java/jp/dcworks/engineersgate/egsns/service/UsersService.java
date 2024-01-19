package jp.dcworks.engineersgate.egsns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.engineersgate.egsns.dto.RequestAccount;
import jp.dcworks.engineersgate.egsns.entity.Users;
import jp.dcworks.engineersgate.egsns.repository.UsersRepository;
import lombok.extern.log4j.Log4j2;

/**
 * ユーザー関連サービスクラス。
 */
@Log4j2
@Service
public class UsersService {

	/** リポジトリインターフェース。 */
	@Autowired
	private UsersRepository repository;

	/**
	 * ユーザー検索を行う。
	 * ユーザーIDを指定し、ユーザーを検索する。
	 *
	 * @param usersId ユーザーID
	 * @return ユーザー情報を返す。
	 */
	public Users findById(String usersId) {
		log.info("ユーザーを検索します。：usersId={}", usersId);

		Long tmpUsersId = null;
		try {
			tmpUsersId = Long.parseLong(usersId);
		} catch (NumberFormatException e) {
			log.warn("ユーザー検索時に不正な値が指定されました。：usersId={}", usersId);
			return null;
		}

		Users users = repository.findById(tmpUsersId).orElse(null);
		log.info("ユーザー検索結果。：usersId={}, users={}", usersId, users);

		return users;
	}

	/**
	 * ユーザー検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @return ユーザー情報を返す。
	 */
	public Users findUsers(String loginId) {
		log.info("ユーザーを検索します。：loginId={}", loginId);

		Users users = repository.findByLoginId(loginId);
		log.info("ユーザー検索結果。：loginId={}, users={}", loginId, users);

		return users;
	}

	/**
	 * ユーザー検索を行う。
	 * ログインID、パスワードを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @param password パスワード
	 * @return ユーザー情報を返す。
	 */
	public Users findUsers(String loginId, String password) {
		log.info("ユーザーを検索します。：loginId={}, password={}", loginId, password);

		Users users = repository.findByLoginIdAndPassword(loginId, password);
		log.info("ユーザー検索結果。：loginId={}, password={}, users={}", loginId, password, users);

		return users;
	}

	/**
	 * ユーザー登録処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	public void save(RequestAccount requestAccount) {
		Users users = new Users();
		users.setLoginId(requestAccount.getLoginId());
		users.setPassword(requestAccount.getPassword());
		users.setName(requestAccount.getName());
		users.setEmailAddress(requestAccount.getEmailAddress());
		repository.save(users);
	}

	/**
	 * ユーザー登録処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	public void save(Users users) {
		repository.save(users);
	}

	/**
	 * ユーザー一覧を取得する。
	 * @return ユーザー一覧
	 */
	public Iterable<Users> findAll() {
		return repository.findAll();
	}
}
