package com.example.demo.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 店舗側の予約設定に関するエンティティ
 */

@Data
@Entity
public class Shop {

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 予約可能開始日
	 * 何日前から予約できるかを設定
	 */
	private Integer reservableDate;

	/**
	 * 予約開始時間
	 */
	private LocalTime startTime;

	/**
	 * 予約終了時間
	 */
	private LocalTime endTime;

	/**
	 * 店休日
	 * 店が休みの曜日を設定
	 */
	private DayOfWeek storeHoliday;
}
