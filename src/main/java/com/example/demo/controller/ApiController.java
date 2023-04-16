package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.ReserveRepository;

@RestController // HTTPリクエストに対してJSONなどのレスポンスを返すために使用されるアノテーション
// @ResponseBodyを付与したメソッドに対してHTTPリクエストが送信された時に自動的にJSONなどの形式でレスポンスが返される
// JSON : データを表現するための軽量のデータ形式の１つで多くのプログラミング言語で扱いやすい汎用的なデータ形式として利用されている
@RequestMapping("/api") // RESTfulなAPIを実装するため使用
// RESTful APIはHTTOリクエストを使用してデータをCRUDするWebサービス
public class ApiController {

	@Autowired
	private ReserveRepository reserveRepository;

	/**
	 * JavaScriptからのAPIエンドポイントでパラメータの日付を受け取ってその日の予約可能な時間を返す
	 * @param date String型の日付
	 * @param session セッションスコープ
	 * @return 予約可能な時間
	 */
	@GetMapping("/availableTimes")
	public List<LocalTime> getAvailableTimes(@RequestParam(value = "date") String date, HttpSession session) {
		//		String型で取得した日付をyyyy/MM/dd形式でLocalDate型にparseする
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		//		日付を条件に予約時間を探す
		List<LocalTime> reservedTimes = reserveRepository.findReserveTimeByReserveDate(localDate);
		List<LocalTime> availableTimes = new ArrayList<>();

		//		timeList = 店舗側設定の予約時間 （例）10:00 ～ 20:00を１時間毎に取得した配列
		@SuppressWarnings("unchecked")
		List<LocalTime> timeList = (List<LocalTime>) session.getAttribute("timeList");

		//		取得した予約情報の予約時間にtimeListの各時間が含まれていなければavailableTimes配列に追加する
		for (LocalTime time : timeList) {
			if (!reservedTimes.contains(time)) {
				availableTimes.add(time);
			}
		}

		//		JavaScriptに予約可能時間 availableTimes配列を返す
		return availableTimes;
	}
}
