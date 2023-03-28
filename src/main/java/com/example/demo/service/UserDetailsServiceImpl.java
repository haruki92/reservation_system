package com.example.demo.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	/**
	 * DBからユーザを検索する
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//		ユーザ名を検索する
		//		ユーザが存在しない場合は、例外をスローする
		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + "not found"));

		//		ユーザ情報を返す
		return new User(user.getUsername(), user.getPassword(),
				AuthorityUtils.createAuthorityList(user.getAuthority().name()));
	}
}
