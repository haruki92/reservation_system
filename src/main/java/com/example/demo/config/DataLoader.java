package com.example.demo.config;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;
import lombok.var;

@RequiredArgsConstructor // finalなフィールドを初期化する
@Component // 部品（コンポーネント）クラスであることを示す	
public class DataLoader implements ApplicationRunner {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	//	Beanの実行に使用される
	@Override
	public void run(ApplicationArguments args) throws Exception {
		//	名前が"admin" パスワードが"password" のユーザを用意する
		var user = new User();
		user.setUsername("admin");
		user.setPassword(passwordEncoder.encode("password"));
		user.setAuthority(Authority.ADMIN);
		user.setAdmin(true);
		user.setEmail("example@example.com");
		user.setPhone("01023456789");
		user.setGender(0);
		user.setIncome(2);
		user.setIndustry(3);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		user.setDeleteFlag(0);

		//		ユーザが存在しない場合、登録する
		if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
			userRepository.save(user);
		}

	}

}
