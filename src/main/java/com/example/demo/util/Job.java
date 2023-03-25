package com.example.demo.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Job {
	public static final Map<Integer, String> INDUSTRY;
	static {
		Map<Integer, String> industry = new LinkedHashMap<>();
		industry.put(1, "製造業");
		industry.put(2, "建築業");
		industry.put(3, "設備業");
		industry.put(4, "運輸業");
		industry.put(5, "流通業");
		industry.put(6, "農林水産業");
		industry.put(7, "印刷・出版業");
		industry.put(8, "金融業・保険業");
		industry.put(9, "不動産業");
		industry.put(10, "IT・情報通信業");
		industry.put(11, "サービス業");
		industry.put(12, "教育・研究機関");
		industry.put(13, "病院・医療機関");
		industry.put(14, "官公庁・自治体");
		industry.put(15, "法人団体");
		industry.put(16, "その他の業種");
		INDUSTRY = Collections.unmodifiableMap(industry);
	}
}
