package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * 予約情報に関するエンティティ
 */
@Data
@Entity
public class Reserve {
	/**
	 * 予約ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 予約日
	 */
	@NotNull(message = "予約日を入力してください")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate reserveDate;

	/**
	 * 予約時間
	 */
	@NotNull(message = "予約時間を入力してください")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime reserveTime;

	/**
	 * ユーザID
	 * Userエンティティのidと関連付け
	 * ユーザ１ 対 予約多
	 */
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user_id;

	/**
	 * 予約変更許可フラグ 初期値 = 0
	 * 予約日前日になったら予約変更不可 = 1
	 */
	private Integer changeFlag;

	/**
	 * 予約取消フラグ 初期値 = 0
	 * 予約取消 = 1
	 */
	private Integer deleteFlag;

	/**
	 * 登録日時
	 */
	@CreatedDate
	private LocalDateTime createdAt;

	/**
	 * 更新日時
	 */
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	/**
	 * 備考
	 * 500文字まで
	 */
	@Size(max = 500)
	private String remarks;
}
