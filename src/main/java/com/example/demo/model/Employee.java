package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

/**
 * 店舗側の従業員に関するエンティティ
 */
@Data
@Entity
public class Employee {

	/**
	 * 従業員ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * ログインID
	 * 重複は許さない
	 */
	@NotBlank
	@Size(min = 6, max = 20)
	@Pattern(regexp = "[a-zA-Z0-9]")
	private String login_id;

	/**
	 * パスワード
	 */
	@NotBlank
	@Size(min = 6, max = 64)
	@Pattern(regexp = "[a-zA-Z0-9]")
	private String password;

	/**
	 * 登録日時
	 */
	@CreatedDate
	private LocalDateTime created_at;

	/**
	 * 更新日時
	 */
	@UpdateTimestamp
	private LocalDateTime updated_at;

	/**
	 * 削除フラグ
	 */
	@NotBlank
	private Integer delete_flag;

	/**
	 * 管理者フラグ
	 */
	@NotBlank
	private Integer admin_flag;
}
