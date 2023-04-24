package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Reserve;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	/**
	 * ユーザIDを条件に予約情報を取得する
	 * @param ユーザID
	 */
	@Query("SELECT r FROM Reserve r WHERE r.user_id.id = ?1")
	Optional<Reserve> findReserveByUser_id(int id);

	/**
	 * deleteFlagが0 = 予約キャンセルされていない予約情報をユーザIDを条件に取得
	 * @param ユーザID
	 */
	@Query("SELECT r FROM Reserve r, User u WHERE r.user_id.id = ?1 AND r.deleteFlag = 0")
	Optional<Reserve> findNotDeletedReserve(int id);

	/**
	 * 予約日を条件に予約情報を取得
	 * @param 予約日
	 * @return 予約情報
	 */
	Optional<Reserve> findByReserveDate(LocalDate reserveDate);

	/**
	 * 予約日時を条件に予約キャンセルされていない予約情報IDを取得する
	 * （取得できる = 予約があるためその日時には予約できない）
	 * @param 日付
	 * @param 時間
	 * @return 予約情報
	 */
	@Query("SELECT r FROM Reserve r WHERE r.reserveDate = ?1 AND r.reserveTime = ?2 AND r.deleteFlag = 0")
	Optional<Reserve> findIdByReserveDateAndReserveTime(LocalDate reserveDate, LocalTime reserveTime);

	/**
	 * 予約日を条件に予約キャンセルされていない予約時間（予約されている時間）を取得
	 * @param date
	 * @return 予約時間
	 */
	@Query("SELECT r.reserveTime FROM Reserve r WHERE r.reserveDate = ?1 AND r.deleteFlag = 0")
	List<LocalTime> findReserveTimeByReserveDate(LocalDate date);

	/**
	 * 予約日を条件に予約キャンセルされていない予約時間（予約されている時間）を取得
	 * @param endDate 
	 * @param startDate 
	 * @param date
	 * @return 予約時間
	 */
	List<Reserve> findByReserveDateBetween(LocalDate startDate, LocalDate endDate);

}
