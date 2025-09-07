package com.sido.backend.stay.utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KoreanAddressParser {
	// 시/도 (광역자치단체) 목록
	private static final String PROVINCE_REGEX =
		"^(서울|부산|대구|인천|광주|대전|울산|세종특별자치시|제주특별자치도|경기|강원특별자치도|충북|충남|전북특별자치도|전남|경북|경남)";

	// 시/군/구 (두 번째 토큰) 패턴: 공백 뒤에 오는 "xx시|xx군|xx구 세종시의 경우 읍|면|동|대로"
	private static final String CITY_REGEX = "\\s([가-힣A-Za-z0-9]+)(시|군|구|읍|면|동|대로)";

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

	//세종, 제주, 강원, 전북 앞에 두 글자만 파싱
	public static Optional<String> extractProvinceShort(String address) {
		return extractProvince(address).map(p -> p.length() <= 2 ? p : p.substring(0, 2));
	}

	//북구, 남구 이런 두 글자의 경우 그대로 내보냄, 종로구 이런 경우 구 기준으로 잘라서 내보냄
	public static Optional<String> extractCityShort(String address) {
		return extractCity(address).map(c -> c.length() <= 2 ? c : c.substring(0, 2));
	}

	private static String normalize(String s) {
		return s.replaceAll("\\s+", " ").trim();
	}
}