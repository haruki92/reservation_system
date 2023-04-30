package com.example.demo.form;

import lombok.Data;

/**
 * 店舗設定フォームの値を受け取る為のフォームオブジェクト
 * エンティティとは異なる
 */
@Data
public class SettingForm {

	//	予約可能期間
	private int reservableDate;

	//	開始時間 時
	private int startHours;

	//	開始時間 分
	private int startMinutes;

	//	終了時間 時
	private int endHours;

	//	終了時間 分
	private int endMinutes;

	//	定休日 storeHolidayだとThymeleafでエンティティで宣言したDayOfWeek型と混同してしまう為名称を変更
	private String dayOff;
}
