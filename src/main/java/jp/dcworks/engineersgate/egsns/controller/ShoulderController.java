package jp.dcworks.engineersgate.egsns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ショルダー確認用コントローラー。
 * ※ショルダー（ヘッダー、フッター、その他ショルダー）の見栄えを確認するためのコントローラー。
 *
 * @author tomo-sato
 */
@Controller
@RequestMapping("/shoulder")
public class ShoulderController {

	@GetMapping(path = {"", "/"})
	public String indes() {
		return "common/shoulder_fragment";
	}
}
