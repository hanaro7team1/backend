package com.sido.backend.stay.utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KoreanAddressParser {
	// 시/도 (광역자치단체) 목록
	private static final String PROVINCE_REGEX =
		"(서울특별시|서울시|서울|부산광역시|부산|대구광역시|대구|인천광역시|인천|광주광역시|광주|대전광역시|대전|울산광역시|울산|세종특별자치시|세종|제주특별자치도|제주|"
			+ "경기도|경기|강원도|강원특별자치도|충청북도|충북|충청남도|충남|전라북도|전북|전북특별자치도|전라남도|전남|경상북도|경북|경상남도|경남)";

	// 시/군/구 (두 번째 토큰) 패턴: 공백 뒤에 오는 "xx시|xx군|xx구"
	private static final String CITY_REGEX = "\\s([가-힣A-Za-z0-9]+)(시|군|구)";

	private static final Pattern PROVINCE_PATTERN = Pattern.compile(PROVINCE_REGEX);
	private static final Pattern CITY_PATTERN = Pattern.compile(CITY_REGEX);

	public static Optional<String> extractProvince(String address) {
		if (address == null)
			return Optional.empty();
		String trimmed = normalize(address);
		Matcher m = PROVINCE_PATTERN.matcher(trimmed);
		return m.find() ? Optional.of(m.group(1)) : Optional.empty();
	}

	public static Optional<String> extractCity(String address) {
		if (address == null)
			return Optional.empty();
		String trimmed = normalize(address);
		Matcher m = CITY_PATTERN.matcher(trimmed);
		return m.find() ? Optional.of(m.group(1)) : Optional.empty();
	}

	private static String normalize(String s) {
		return s.replaceAll("\\s+", " ").trim();
	}
}