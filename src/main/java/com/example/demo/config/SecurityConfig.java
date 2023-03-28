package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // finalなフィールドを初期化する
@Configuration // 構成クラスであることを示す
public class SecurityConfig {

	/**
	 * パスワードを暗号化する
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		//		パスワードの暗号化にBcryptを使用する
		return new BCryptPasswordEncoder();
	}

	/**
	 * 認証アクションの設定
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				//		認証リクエストの設定
				.authorizeHttpRequests(auth -> auth
						//				cssやjsなどの静的リソースをアクセス可能にする
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						//				/register と /login と /confirm をアクセス可能にする
						.mvcMatchers("/register", "/login", "/confirm").permitAll()
						//				/adminの配下はADMINユーザだけアクセス可能にする
						.mvcMatchers("/admin/**").hasAuthority(Authority.ADMIN.name())
						.anyRequest()
						.authenticated())
				.formLogin(login -> login
						//						ログイン時のURLを設定
						.loginPage("/login")
						//						認証後にリダイレクトする場所を指定
						.defaultSuccessUrl("/")
						.permitAll())
				//				ログアウト時の設定
				.logout(logout -> logout
						//						ログアウト時のURL
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.permitAll())
				//				Remember-Meの設定
				.rememberMe();

		return http.build();
	}
}
