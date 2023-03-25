package com.example.demo.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Income {

	public static final Map<Integer, String> INCOME;

	static {
		Map<Integer, String> income = new LinkedHashMap<>();
		income.put(1, "〜 300万円");
		income.put(2, "300 〜 400万円");
		income.put(3, "400 〜 600万円");
		income.put(4, "600 〜 800万円");
		income.put(5, "800 〜 1000万円");
		income.put(6, "1000 〜 1200万円");
		income.put(7, "1200万円 〜");
		INCOME = Collections.unmodifiableMap(income);

	}
}
