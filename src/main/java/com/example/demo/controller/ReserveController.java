package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.config.ApplicationScope;
import com.example.demo.converter.LocalDateConverter;
import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ReserveService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ReserveController {

	private final ReserveService reserveService;

	@Autowired
	private final UserRepository userRepository;

	@GetMapping("/reserve")
	public String showReserveForm(Reserve reserve, HttpSession session) {
		Integer reservableDate = (Integer) ApplicationScope.getAttribute("reservableDate");
		LocalTime startTime = (LocalTime) ApplicationScope.getAttribute("startTime");
		LocalTime endTime = (LocalTime) ApplicationScope.getAttribute("endTime");
		long limit = endTime.getHour() - startTime.getHour(); // startTimeからendTimeの差分

		List<LocalTime> timeList = Stream.iterate(startTime, t -> t.plusHours(1))
				.limit(limit + 1)
				.collect(Collectors.toList()); // 予約時間のリスト

		Calendar calendar = Calendar.getInstance();
		List<LocalDate> dates = new ArrayList<>(); // 予約可能期間のリスト

		for (int i = 0; i < reservableDate; i++) {
			dates.add(LocalDateConverter.dateToLocalDate(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}

		session.setAttribute("timeList", timeList);
		session.setAttribute("dates", dates);
		return "reserve/reserve";

	}

	@GetMapping("/reserve/complete")
	public String completeReserve(RedirectAttributes redirectAttributes, HttpSession session) {
		redirectAttributes.addFlashAttribute("reserved", "予約が完了しました");

		session.removeAttribute("reserve");
		return "redirect:/";
	}

	@PostMapping("/reserve/complete")
	public String createReserve(Authentication loginUser, HttpSession session) {
		Reserve reserve = (Reserve) session.getAttribute("reserve");
		User user = (User) userRepository.findByUsername(loginUser.getName()).get();

		try {
			//			予約する
			reserveService.reserve(reserve, user);
		} catch (Exception e) {
			//			予約中にエラーが発生した場合
			return "redirect:/reserve?error"; // errorパラメータを付与して予約画面にリダイレクト
		}

		session.setAttribute("reserve", reserve);

		return "redirect:/reserve/complete";
	}

	@GetMapping("/reserve/confirm")
	public String showReserveComfirm(Model model, HttpSession session) {
		Reserve reserve = (Reserve) session.getAttribute("reserve");

		model.addAttribute("reserve", reserve);
		return "reserve/confirm"; // 予約確認画面に遷移
	}

	@PostMapping("/reserve/confirm")
	public String postReserveConfirm(@Validated Reserve reserve, BindingResult result, HttpSession session) {

		if (result.hasErrors()) {
			System.err.println("reserveにエラーがありました");
			return "reserve/reserve";
		}

		//		予約の重複がないか確認
		if (!reserveService.isReservationAvailable(reserve.getReserveDate(), reserve.getReserveTime())) {
			//			予約の重複がある場合
			return "redirect:/reserve?error"; // errorパラメータを持たせて予約画面へ遷移
		}

		session.setAttribute("reserve", reserve);

		return "reserve/confirm"; // 予約確認画面に遷移
	}

}