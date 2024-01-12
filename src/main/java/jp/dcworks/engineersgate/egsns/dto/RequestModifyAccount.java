package jp.dcworks.engineersgate.egsns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * アカウント編集DTOクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestModifyAccount extends DtoBase {

	/** お名前 */
	@NotBlank(message = "お名前を入力してください。")
	@Size(max = 32, message = "お名前は最大32文字です。")
	private String name;

	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください。")
	@Size(max = 256, message = "メールアドレスは最大256文字です。")
	private String emailAddress;

	/** プロフィール */
	@Size(max = 2000, message = "プロフィールは最大2000文字です。")
	private String profile;

	/** 設定済みのファイル */
	private String profileFileHidden;
}
