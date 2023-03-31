package com.example.demo.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class SecurityController {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/")
	public String success(Authentication loginUser, HttpSession session) {
		session.setAttribute("username", loginUser.getName());
		session.setAttribute("authority", loginUser.getAuthorities());

		return "user";
	}

	@GetMapping("/register")
	public String register(User user) {
		return "register";
	}

	@PostMapping("/register")
	public String complete(HttpSession session) {
		User user = (User) session.getAttribute("user");

		//		パスワードを暗号化してセット
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		//		その他ユーザ情報をセット
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		user.setDeleteFlag(0);
		user.setAuthority(Authority.USER); // authorityをuserに

		//		セッションからuserオブジェクトを削除する
		session.removeAttribute("user");

		//		 ユーザ情報を保存
		userRepository.save(user);

		return "redirect:/login?register";
	}

	@GetMapping("/confirm")
	public String getConfirm() {
		return "confirm";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated User user, BindingResult result, HttpSession session) {
		if (result.hasErrors()) {
			return "register";
		}

		//		性別が未選択の場合、"選択しない"をセット
		if (user.getGender() == null) {
			user.setGender(0);
		}

		session.setAttribute("user", user);

		return "redirect:/confirm";
	}

}
