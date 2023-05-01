package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApplicationScope;
import com.example.demo.converter.DayOfWeekConverter;
import com.example.demo.form.SettingForm;
import com.example.demo.model.Shop;
import com.example.demo.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShopService {

	@Autowired
	private ShopRepository shopRepository;

	/**
	 * 店舗側の各種設定をアプリケーションスコープに登録する
	 */
	@PostConstruct // アプリケーション起動時に自動的にメソッドを呼び出す
	public void registerShopSettingToApplicationScope() {
		//		店舗側の設定用エンティティを取得する
		Shop shop = (Shop) shopRepository.findById(1).get();

		//	    今日から店舗側設定の予約可能日分を加算する
		Integer reservableDate = shop.getReservableDate();
		//		店舗側設定の予約開始時間を取得
		LocalTime startTime = shop.getStartTime();
		//		店舗側設定の予約終了時間を取得
		LocalTime endTime = shop.getEndTime();
		//		店舗側の設定の定休日を取得
		DayOfWeek storeHoliday = shop.getStoreHoliday();

		Integer timeInterval = shop.getTimeInterval();

		ApplicationScope.setAttribute("reservableDate", reservableDate); // 予約可能日
		ApplicationScope.setAttribute("startTime", startTime); // 予約開始時間
		ApplicationScope.setAttribute("endTime", endTime); // 予約終了時間
		ApplicationScope.setAttribute("storeHoliday", storeHoliday); // 店休日
		ApplicationScope.setAttribute("timeInterval", timeInterval); // 予約時間間隔 分
	}

	/**
	 * 店舗設定のフォームオブジェクトで受け取った値を更新する
	 * @param settingForm
	 */
	public void updateShopSettings(SettingForm settingForm) {
		Shop shop = (Shop) shopRepository.findById(1).get();
		shop.setReservableDate(settingForm.getReservableDate());
		shop.setStartTime(LocalTime.of(settingForm.getStartHours(), settingForm.getStartMinutes()));
		shop.setEndTime(LocalTime.of(settingForm.getEndHours(), settingForm.getEndMinutes()));
		shop.setTimeInterval(settingForm.getTimeInterval());
		shop.setStoreHoliday(DayOfWeekConverter.stringToDayOfWeek(settingForm.getDayOff()));

		shopRepository.save(shop);
		registerShopSettingToApplicationScope();
	}

}
