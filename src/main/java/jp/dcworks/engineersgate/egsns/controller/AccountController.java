package jp.dcworks.engineersgate.egsns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ※TODO 適宜実装を入れてください。
 */
@Controller
@RequestMapping("/account")
public class AccountController {

	@GetMapping(path = {"", "/"})
	public String index() {
		return "/account/index";
	}

	@GetMapping("/complete")
	public String complete() {
		return "/account/complete";
	}
}
