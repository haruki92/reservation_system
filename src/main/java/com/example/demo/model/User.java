package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;

import com.example.demo.util.Authority;
import com.example.demo.validator.UniqueLogin;

import lombok.Data;

/**
 * ユーザ情報に関するエンティティ
 */
@Data
@Entity
public class User {
	/**
	 * 顧客ID（主キー）
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * ユーザ名
	 */
	@Size(min = 2, max = 40)
	@UniqueLogin // 自作バリデーション
	private String username;

	/**
	 * パスワード
	 */
	@Size(min = 6, max = 64)
	private String password;

	/**
	 * 権限
	 */
	private Authority authority;

	/**
	 * 管理者
	 */
	private boolean admin;

	/**
	 * メールアドレス
	 */
	@NotBlank
	@Email
	@Size(max = 254)
	private String email;

	/**
	 * 電話番号
	 */
	@NotBlank
	@Pattern(regexp = "[0-9-]*", message = "半角数字で入力してください")
	private String phone;

	/**
	 * 性別
	 */
	@Range(min = 0, max = 3, message = "性別を選択してください")
	private Integer gender;

	/**
	 * 年収
	 */
	@Range(min = 1, max = 7, message = "年収を選択してください")
	private Integer income;

	/**
	 * 業種
	 */
	@Range(min = 1, max = 16, message = "業種を選択してください")
	private Integer industry;

	/**
	 * 登録日時
	 * Entityが生成され、保存される時に時間が自動で保存される
	 */
	@CreatedDate
	private LocalDateTime createdAt;

	/**
	 * 更新日時
	 * updateTimestampはテーブルの内容に変更がない場合はupdateしない
	 * LastModifiedDateはEntityの値を変更する時に時間が自動で保存される
	 */
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	/**
	 * 削除フラグ
	 */
	private Integer deleteFlag;
}
