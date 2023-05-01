package com.example.demo.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
        Integer timeInterval = (Integer) ApplicationScope.getAttribute("timeInterval");
        LocalTime startTime = (LocalTime) ApplicationScope.getAttribute("startTime");
        LocalTime endTime = (LocalTime) ApplicationScope.getAttribute("endTime");
        DayOfWeek storeHoliday = (DayOfWeek) ApplicationScope.getAttribute("storeHoliday");

		// startTimeからendTimeまでの時間(h)を返すメソッド
		List<LocalTime> hoursList = reserveService.createHoursList(startTime, endTime);

		// 予約時間(min)を返すメソッド
		List<LocalTime> minutesList = reserveService.createMinutesList(startTime, endTime, timeInterval);

		// 予約可能な日付を返すメソッド
		List<LocalDate> dates = reserveService.createReservableDates(reservableDate, storeHoliday);

        session.setAttribute("hoursList", hoursList);
		session.setAttribute("minutesList", minutesList);
        session.setAttribute("dates", dates);
        return "reserve/reserve";

    }

    @GetMapping("/reserve/complete")
    public String completeReserve(RedirectAttributes redirectAttributes, HttpSession session) {
        redirectAttributes.addFlashAttribute("flush", "予約が完了しました");

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

        return "redirect:/reserve/complete";
    }

    @GetMapping("/reserve/confirm")
    public String showReserveConfirm(Model model, HttpSession session) {
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

    /**
     * 予約変更画面への遷移
     * @param rs 予約情報
     * @param session セッションスコープ
     * @return 予約変更画面
     */
    @GetMapping("/reserve/edit")
    public String getEdit(Reserve rs, HttpSession session) {
        Integer reservableDate = (Integer) ApplicationScope.getAttribute("reservableDate");
		Integer timeInterval = (Integer) ApplicationScope.getAttribute("timeInterval");
        LocalTime startTime = (LocalTime) ApplicationScope.getAttribute("startTime");
        LocalTime endTime = (LocalTime) ApplicationScope.getAttribute("endTime");
		DayOfWeek storeHoliday = (DayOfWeek) ApplicationScope.getAttribute("storeHoliday");

		// startTimeからendTimeまでの時間(h)を返すメソッド
		List<LocalTime> hoursList = reserveService.createHoursList(startTime, endTime);

		// 予約時間(min)を返すメソッド
		List<LocalTime> minutesList = reserveService.createMinutesList(startTime, endTime, timeInterval);

		// 予約可能な日付を返すメソッド
		List<LocalDate> dates = reserveService.createReservableDates(reservableDate, storeHoliday);

		session.setAttribute("hoursList", hoursList);
        session.setAttribute("minutesList", minutesList);
        session.setAttribute("dates", dates);

        return "reserve/edit";
    }

    @GetMapping("/reserve/editConfirm")
    public String getEditConfirm(Model model, HttpSession session) {
        Reserve reserve = (Reserve) session.getAttribute("reserve");

        model.addAttribute("reserve", reserve);
        return "reserve/editConfirm"; // 予約確認画面に遷移
    }

    @PostMapping("/reserve/editConfirm")
    public String postEdit(@Validated Reserve reserve, BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            System.err.println("reserveにエラーがありました");
            return "reserve/edit";
        }
        //		予約の重複がないか確認
        if (!reserveService.isReservationAvailable(reserve.getReserveDate(), reserve.getReserveTime())) {
            //			予約の重複がある場合
            return "redirect:/edit?error"; // errorパラメータを持たせて予約変更画面へ遷移
        }

        session.setAttribute("reserve", reserve);

        return "reserve/editConfirm"; // 予約変更確認画面に遷移
    }

    @GetMapping("/reserve/editComplete")
    public String getEditComplete(RedirectAttributes redirectAttributes, HttpSession session) {
        redirectAttributes.addFlashAttribute("flush", "予約変更が完了しました");

        session.removeAttribute("reserve");
        return "redirect:/";
    }

    @PostMapping("/reserve/editComplete")
    public String postEditComplete(Authentication loginUser, HttpSession session) {
        System.out.println("ReserveController.postEditComplete()");
        Reserve reserve = (Reserve) session.getAttribute("reserve");

        User user = (User) userRepository.findByUsername(loginUser.getName()).get();

        try {
            //			予約する
            reserveService.changeReserve(reserve, user);
        } catch (Exception e) {
            //			予約中にエラーが発生した場合
            System.err.println(e.getMessage());
            return "redirect:/edit?error"; // errorパラメータを付与して予約画面にリダイレクト
        }

        return "redirect:/reserve/editComplete";
    }

    @GetMapping("/reserve/delete")
    public String getReserveDelete(Model model, HttpSession session) {
        Reserve reserve = (Reserve) session.getAttribute("reserve");
        model.addAttribute("reserve", reserve);

        return "reserve/delete";
    }

    @PostMapping("/reserve/delete")
    public String reserveDelete(RedirectAttributes redirectAttributes, HttpSession session) {
        Reserve reserve = (Reserve) session.getAttribute("reserve");

        try {
            reserveService.deleteReserve(reserve);
            redirectAttributes.addFlashAttribute("flush", "予約をキャンセルしました");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "redirect:/?error";
        }

        session.removeAttribute("reserve");

        return "redirect:/";
    }

}