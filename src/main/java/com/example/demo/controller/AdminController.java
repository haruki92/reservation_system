package com.example.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.config.ApplicationScope;
import com.example.demo.converter.DayOfWeekConverter;
import com.example.demo.form.SettingForm;
import com.example.demo.model.Reserve;
import com.example.demo.model.User;
import com.example.demo.repository.ReserveRepository;
import com.example.demo.service.ReserveService;
import com.example.demo.service.ShopService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

	private final ReserveRepository reserveRepository;

	@Autowired
	ShopService shopService;
	ReserveService reserveService;

	@GetMapping("/setting")
	public String getSetting(SettingForm settingForm, Model model) {
		Integer reservableDate = (Integer) ApplicationScope.getAttribute("reservableDate");
		LocalTime startTime = (LocalTime) ApplicationScope.getAttribute("startTime");
		LocalTime endTime = (LocalTime) ApplicationScope.getAttribute("endTime");
		Integer timeInterval = (Integer) ApplicationScope.getAttribute("timeInterval");

		int startHours = startTime.getHour();
		int startMinutes = startTime.getMinute();
		int endHours = endTime.getHour();
		int endMinutes = endTime.getMinute();

		//		アプリケーションスコープの店休日を取得してビューに渡す
		DayOfWeek storeHoliday = (DayOfWeek) ApplicationScope.getAttribute("storeHoliday");

		switch (storeHoliday) {
		case SUNDAY:
			settingForm.setDayOff("日");
			model.addAttribute("storeHoliday", "日");
			break;

		case MONDAY:
			settingForm.setDayOff("月");
			model.addAttribute("storeHoliday", "月");
			break;

		case TUESDAY:
			settingForm.setDayOff("火");
			model.addAttribute("storeHoliday", "火");
			break;

		case WEDNESDAY:
			settingForm.setDayOff("水");
			model.addAttribute("storeHoliday", "水");
			break;

		case THURSDAY:
			settingForm.setDayOff("木");
			model.addAttribute("storeHoliday", "木");
			break;

		case FRIDAY:
			settingForm.setDayOff("金");
			model.addAttribute("storeHoliday", "金");
			break;

		case SATURDAY:
			settingForm.setDayOff("土");
			model.addAttribute("storeHoliday", "土");
			break;
		}

		//		DayOfWeekの値を曜日（漢字）に変更する
		DayOfWeek[] dayOfWeek = DayOfWeek.values();
		List<String> dayNames = new ArrayList<>();
		for (DayOfWeek dow : dayOfWeek) {
			dayNames.add(DayOfWeekConverter.dayOfWeekToString(dow));
		}

		//		フォームオブジェクトに初期値を設定
		settingForm.setReservableDate(reservableDate);
		settingForm.setStartHours(startHours);
		settingForm.setStartMinutes(startMinutes);
		settingForm.setEndHours(endHours);
		settingForm.setEndMinutes(endMinutes);
		settingForm.setTimeInterval(timeInterval);

		model.addAttribute("reservableDate", reservableDate);
		model.addAttribute("startHours", startHours);
		model.addAttribute("startMinutes", startMinutes);
		model.addAttribute("endHours", endHours);
		model.addAttribute("endMinutes", endMinutes);
		model.addAttribute("timeInterval", timeInterval);
		model.addAttribute("dayOfWeek", dayNames);

		return "admin/setting";
	}

	@PostMapping("/setting")
	public String changeSettings(@ModelAttribute("settingForm") SettingForm settingForm,
			RedirectAttributes redirectAttributes) {
		shopService.updateShopSettings(settingForm);

		redirectAttributes.addFlashAttribute("flush", "設定を変更しました");

		return "redirect:/";
	}

	@GetMapping("/details")
	public String getDetails(@RequestParam("user_id") User user_id, Model model) {
		Reserve reserve = (Reserve) reserveRepository.findReserveByUser_id(user_id.getId()).get();
		model.addAttribute("user", user_id);
		model.addAttribute("reserve", reserve);

		return "admin/reserveDetails";
	}
}
