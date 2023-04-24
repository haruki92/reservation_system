package com.example.demo.config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Reserve;
import com.example.demo.model.Shop;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;
import com.example.demo.repository.ShopRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Authority;

import lombok.RequiredArgsConstructor;
import lombok.var;

@RequiredArgsConstructor // finalなフィールドを初期化する
@Component // 部品（コンポーネント）クラスであることを示す	
public class DataLoader implements ApplicationRunner {
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ShopRepository shopRepository;
	private final ReserveRepository reserveRepository;

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
		if (userRepository.findByUsername("admin").isEmpty()) {
			userRepository.save(user);
		}

		var reserve = new Reserve();
		reserve.setReserveDate(LocalDate.of(2026, 8, 13));
		reserve.setReserveTime(LocalTime.of(14, 0));
		reserve.setUser_id(user);
		reserve.setChangeFlag(0);
		reserve.setDeleteFlag(0);
		reserve.setCreatedAt(LocalDateTime.now());
		reserve.setUpdatedAt(LocalDateTime.now());
		reserve.setRemarks("adminのtestReservation");
		if (reserveRepository.findReserveByUser_id(user.getId()).isEmpty())
			reserveRepository.save(reserve);

		//		店舗側の設定
		var shop = new Shop();
		shop.setReservableDate(7); // 予約可能日時 7 日後
		shop.setStartTime(LocalTime.of(10, 0)); // 予約開始時間 10:00 ～
		shop.setEndTime(LocalTime.of(20, 0)); // 予約終了時間 ～ 20:00
		shop.setStoreHoliday(DayOfWeek.SUNDAY); // 店休日 日曜日
		if (shopRepository.findById(1).isEmpty()) {
			shopRepository.save(shop);
		}
	}

}
