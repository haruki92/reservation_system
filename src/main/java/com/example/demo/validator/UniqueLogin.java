package com.example.demo.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD }) // このアノテーションが使える場所を指定する メソッドとフィールドで使うことができる
@Retention(RetentionPolicy.RUNTIME) // このアノテーションの保持期間（ポリシー）を指定する RUNTIME = 実行時にVNに保持される = いつでも使える
@Constraint(validatedBy = UniqueLoginValidator.class) // このアノテーションのチェック対象を指定する validateByでチェックロジックを実装したクラスを指定する

// @interfaceと宣言することで@UniqueLoginという自作のアノテーションを使えるようになる
public @interface UniqueLogin {
	String message() default "このユーザ名は既に登録されています";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
