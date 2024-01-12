package jp.dcworks.engineersgate.egsns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * コメント投稿DTOクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestShare extends DtoBase {

	/** タイトル */
	@NotBlank(message = "タイトルを入力してください。")
	@Size(max = 512, message = "タイトルは最大512文字です。")
	private String title;

	/** 本文 */
	@NotBlank(message = "コメント本文を入力してください。")
	@Size(max = 2000, message = "コメント本文は最大2000文字です。")
	private String body;
}
