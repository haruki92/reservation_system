package com.example.demo.converter;

import java.time.DayOfWeek;

public class DayOfWeekConverter {
	/**
	 * DayOfWeekからStringに変換する
	 * @param DayOfWeekオブジェクト
	 * @return String
	 */
	public static String dayOfWeekToString(DayOfWeek dayOfWeek) {
		String dayNames = null;

		switch (dayOfWeek) {
		case SUNDAY:
			dayNames = "日";
			break;
		case MONDAY:
			dayNames = "月";
			break;
		case TUESDAY:
			dayNames = "火";
			break;
		case WEDNESDAY:
			dayNames = "水";
			break;
		case THURSDAY:
			dayNames = "木";
			break;
		case FRIDAY:
			dayNames = "金";
			break;
		case SATURDAY:
			dayNames = "土";
			break;
		}

		return dayNames;
	}

	/**
	 * StringからDayOfWeekに変換する
	 * @param String
	 * @return DayOfWeekオブジェクト
	 */
	public static DayOfWeek stringToDayOfWeek(String str) {
		DayOfWeek dayOfWeek = null;

		switch (str) {
		case "日":
			dayOfWeek = DayOfWeek.SUNDAY;
			break;

		case "月":
			dayOfWeek = DayOfWeek.MONDAY;
			break;

		case "火":
			dayOfWeek = DayOfWeek.TUESDAY;
			break;

		case "水":
			dayOfWeek = DayOfWeek.WEDNESDAY;
			break;

		case "木":
			dayOfWeek = DayOfWeek.THURSDAY;
			break;

		case "金":
			dayOfWeek = DayOfWeek.FRIDAY;
			break;

		case "土":
			dayOfWeek = DayOfWeek.SATURDAY;
			break;

		}

		return dayOfWeek;
	}
}
