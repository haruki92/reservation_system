package com.example.demo.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.converter.LocalDateConverter;
import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;

@Transactional // メソッドがトランザクションで実行される事を指定する
@Service // サービスクラスであることを示す
public class ReserveService {

	@Autowired // 自動的にインスタンスを生成
	private ReserveRepository reserveRepository;

	/**
	 * 予約情報を登録するメソッド
	 * @param 予約情報
	 * @param ユーザ情報
	 */
	public void reserve(Reserve reserve, User user) {
		reserve.setUser_id(user);
		reserve.setChangeFlag(0);
		reserve.setDeleteFlag(0);
		reserve.setCreatedAt(LocalDateTime.now());
		reserve.setUpdatedAt(LocalDateTime.now());
		reserveRepository.save(reserve);
	}

	/**
	 * 予約可能か判別するメソッド
	 * @param 予約情報
	 * @return 予約の可否
	 */
	public boolean isReservationAvailable(LocalDate reserveDate, LocalTime reserveTime) {

		//		エンティティから日時を条件に予約情報を取得する
		Optional<Reserve> existingReservation = reserveRepository.findIdByReserveDateAndReserveTime(reserveDate,
				reserveTime);

		//		予約情報が空の場合 true を返す
		return existingReservation.isEmpty();
	}

	/**
	 * 予約を変更するメソッド
	 * @param 変更後の予約情報
	 * @param ユーザ情報
	 */
	public void changeReserve(Reserve reserve, User user) {
		Reserve rs = reserveRepository.findNotDeletedReserve(user.getId()).get();
		rs.setReserveDate(reserve.getReserveDate());
		rs.setReserveTime(reserve.getReserveTime());
		rs.setRemarks(reserve.getRemarks());
		rs.setChangeFlag(1);
		rs.setUpdatedAt(LocalDateTime.now());
		reserveRepository.save(rs);
	}

	/**
	 * 予約を削除するメソッド
	 * @param reserve 削除対象の予約情報
	 */
	public void deleteReserve(Reserve reserve) {
		Reserve rs = reserveRepository.findNotDeletedReserve(reserve.getUser_id().getId()).get();
		rs.setDeleteFlag(1);
		rs.setUpdatedAt(LocalDateTime.now());
		reserveRepository.save(rs);
	}

	/**
	 * startTimeからendTimeまでの時間(h)を返すメソッド
	 * @param startTime 開始時間
	 * @param endTime 終了時間
	 * @return hoursList 時間(h)の配列
	  */
	public List<LocalTime> createHoursList(LocalTime startTime, LocalTime endTime) {
		List<LocalTime> hoursList = new ArrayList<>();
		LocalTime currentHour = startTime;
		while (currentHour.isAfter(startTime.minusNanos(1)) && currentHour.isBefore(endTime)
				&& !(currentHour.equals(LocalTime.MIDNIGHT))) {
			if (currentHour.isAfter(LocalTime.MIDNIGHT) && currentHour.isBefore(startTime)) {
				System.out.println("00:00を過ぎてstartTimeの前なのでループ処理を終了します。");
				break;
			}
			hoursList.add(currentHour);
			currentHour = currentHour.plusHours(1);
		}

		return hoursList;
	}

	/**
	 * 予約時間(min)のリストを返す
	 * @param startTime 開始時間
	 * @param endTime 終了時間
	 * @param timeInterval 予約時間間隔
	 * @return minutesList 予約時間(min)のリスト
	 */
	public List<LocalTime> createMinutesList(LocalTime startTime, LocalTime endTime, Integer timeInterval) {
		// 予約時間（分）のリスト
		List<LocalTime> minutesList = new ArrayList<>();

		// startTimeからendTimeまでの時間の差分を計算
		Duration duration = Duration.between(startTime, endTime);

		// durationを分にして取得
		long durationMinutes = duration.toMinutes();
		LocalTime currentTime = startTime;

		// durationの分数を予約時間感覚で割った値 = endTimeまでに予約できる最大の回数
		//（例）980分 15分間隔 980 / 15 = 65.3333... => 最大65回予約できる
		// 予約できる時間だけminutesListに追加する
		for (int i = 0; i <= durationMinutes / timeInterval; i++) {
			minutesList.add(currentTime);
			currentTime = currentTime.plusMinutes(timeInterval);
		}

		return minutesList;
	}

	/**
	 * 予約可能な日付を返すメソッド
	 * @param reservableDate 予約可能期間
	 * @param storeHoliday 定休日
	 * @return dates 予約可能な日付
	 */
	public List<LocalDate> createReservableDates(Integer reservableDate, DayOfWeek storeHoliday) {
		// 予約可能期間のリスト
		List<LocalDate> dates = new ArrayList<>();
		// Calender型のインスタンスを作成（現在の日時）
		Calendar calender = Calendar.getInstance();

		// 現在の日時から予約可能期間の分だけdatesリストに日付を追加する
		// ただし、定休日は追加しない
		for (int i = 0; i < reservableDate; i++) {
			int calDOW = calender.get(Calendar.DAY_OF_WEEK) - 1;

			if (calDOW == 0) {
				calDOW = 7;
			}

			DayOfWeek dow = DayOfWeek.of(calDOW);

			//			曜日が定休日ではない時にのみdatesリストに追加する
			if (!(dow == storeHoliday)) {
				dates.add(LocalDateConverter.dateToLocalDate(calender.getTime()));
			}

			calender.add(Calendar.DATE, 1);
		}

		return dates;
	}

}
