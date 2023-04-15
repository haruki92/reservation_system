package com.example.demo.converter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateConverter {
	/**
	 * DateオブジェクトからLocalDateに変換する
	 * @param Dateオブジェクト
	 * @return LocalDateオブジェクト
	 */
	public static LocalDate dateToLocalDate(final Date date) {
		LocalDateTime localDateTime = new Timestamp(date.getTime()).toLocalDateTime();
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}

	/**
	 * LocalDateオブジェクトからDateに変換する
	 * @param LocalDateオブジェクト
	 * @return Dateオブジェクト
	 */
	public static Date localDateToDate(final LocalDate localDate) {

		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
}
