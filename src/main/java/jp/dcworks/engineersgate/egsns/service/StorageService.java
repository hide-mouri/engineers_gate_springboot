package jp.dcworks.engineersgate.egsns.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import lombok.extern.log4j.Log4j2;

/**
 * ファイルアップロード関連サービスクラス。
 */
@Log4j2
@Service
public class StorageService {

	/**
	 * ファイルアップロード処理。
	 *
	 * @param multipartFile マルチパートで受信したファイル。
	 * @return ファイルアップロード先の、Webサイトで見たときのドキュメントルートからの相対パスが返却される。
	 */
	public String store(MultipartFile multipartFile) {

		// ファイル名取得
		String fileName = multipartFile.getOriginalFilename();

		if (StringUtils.isEmpty(fileName)) {
			return null;
		}

		// 格納先の相対パス
		Path filePath = Paths.get("src/main/resources/static/assets/profileimg/" + fileName);

		OutputStream stream = null;
		try {

			File file = new File(filePath.toString());
			if (file.exists()) {
				return "/assets/profileimg/" + fileName;
			}

			// バイト値を書き込む為のファイルを作成して指定したパスに格納
			stream = Files.newOutputStream(filePath);

			// アップロードファイルをバイト値に変換
			byte[] bytes = multipartFile.getBytes();

			// ファイルに書き込み
			stream.write(bytes);
		} catch (IOException e) {
			log.error("ファイルアップロード中にエラーが発生しました。", e);
			return null;
		} finally {
			if (stream != null) {
				try {
					stream.close();

				} catch (IOException e) {
					log.error("ファイルアップロードのクローズ処理でエラーが発生しました。", e);
				}
			}
		}

		return "/assets/profileimg/" + fileName;
	}

	/**
	 * ファイルチェック処理。
	 * 画像ファイルであるか、否かのチェックを行う。
	 *
	 * @param multipartFile マルチパートで受信したファイル。
	 * @return true.画像ファイルである。false.画像ファイルではない。（または、処理中にエラーが発生した場合。）
	 */
	public static boolean isImageFile(MultipartFile multipartFile) {
		// ファイル名取得
		String fileName = multipartFile.getOriginalFilename();

		if (StringUtils.isEmpty(fileName)) {
			// 画像ファイル名称が取得できない場合は、デフォルト画像を設定するため「true」を返す。
			return true;
		}

		FileOutputStream fos = null;
		try {
			File file = new File("src/main/resources/static/assets/profileimg/" + fileName);
			// 拡張子を除いたファイル名を取得
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(multipartFile.getBytes());
			fos.close();

			if (file != null && file.isFile()) {
				BufferedImage bi = ImageIO.read(file);
				if (bi != null) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error("ファイルチェック中にエラーが発生しました。", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					log.error("ファイルチェック処理中のクローズ処理でエラーが発生しました。", e);
				}
			}
		}
	}

	/**
	 * 画像のバイナリ情報を返す。
	 * ※アップロードではビルドパスに画像情報を配置している為、アップロード直後にリビルドがかからず画像がデプロイされない。
	 * 　その為リンク切れが発生していた。
	 * 　直接ファイルを指定して、バイナリ情報を画面に送ることで解消。
	 * 
	 * @param imageUri 画像URI
	 * @return 画像のバイナリ情報。
	 */
	public static String getDataUri(String imageUri) {
		String base64EncodedImage = "";
		try {
			byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/static/" + imageUri));

			// バイト配列をBase64エンコード
			base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "data:image/jpeg;base64," + base64EncodedImage;
	}
}
