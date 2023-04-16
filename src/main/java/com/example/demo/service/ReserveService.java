package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
