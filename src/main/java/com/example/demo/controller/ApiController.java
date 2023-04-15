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

	@GetMapping("/availableTimes")
	public List<LocalTime> getAvailableTimes(@RequestParam(value = "date") String date, HttpSession session) {
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		List<LocalTime> reservedTimes = reserveRepository.findReserveTimeByReserveDate(localDate);
		List<LocalTime> availableTimes = new ArrayList<>();

		@SuppressWarnings("unchecked")
		List<LocalTime> timeList = (List<LocalTime>) session.getAttribute("timeList");

		for (LocalTime time : timeList) {
			if (!reservedTimes.contains(time)) {
				availableTimes.add(time);
			}
		}

		return availableTimes;
	}
}
