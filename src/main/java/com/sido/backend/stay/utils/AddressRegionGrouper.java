package com.sido.backend.stay.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sido.backend.stay.dto.RegionResponseDTO;

@Component
public class AddressRegionGrouper {
	//한국어 정렬 규칙(사전식 비교)
	private static final Collator KO = Collator.getInstance(Locale.KOREAN);

	public List<RegionResponseDTO> groupByProvinceFromAddresses(List<String> addresses) {
		Map<String, Set<String>> map = new HashMap<>();

		for (String addr : addresses) {
			String province = KoreanAddressParser.extractProvince(addr).orElse(null);
			String city = KoreanAddressParser.extractCity(addr).orElse(null);
			if (province == null || city == null)
				continue;
			map.computeIfAbsent(province, k -> new HashSet<>()).add(city);
		}

		return map.entrySet().stream()
			.map(e -> new RegionResponseDTO(
				KoreanAddressParser.extractProvinceShort(e.getKey()).orElse(e.getKey()), // provinceShort
				e.getValue().stream()
					.map(c -> KoreanAddressParser.extractCityShort(c).orElse(c)) // cityShorts
					.sorted(KO)
					.collect(Collectors.toList())
			))
			.sorted(Comparator.comparing(RegionResponseDTO::getRegion, KO))
			.collect(Collectors.toList());
	}
}