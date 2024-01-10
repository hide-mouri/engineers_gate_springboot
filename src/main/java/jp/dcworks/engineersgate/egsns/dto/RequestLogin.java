package jp.dcworks.engineersgate.egsns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ログインDTOクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestLogin extends DtoBase {

	/** ログインID */
	@NotBlank(message = "ログインIDを入力してください。")
	@Size(max = 32, message = "お名前は最大32文字です。")
	private String loginId;

	/** パスワード */
	@NotBlank(message = "パスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String password;
}
