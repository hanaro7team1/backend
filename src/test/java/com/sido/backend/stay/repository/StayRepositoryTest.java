package com.sido.backend.stay.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sido.backend.RepositoryTest;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.entity.StayAvailDate;

class StayRepositoryTest extends RepositoryTest {
	@Autowired
	private StayRepository stayRepository;

	@Autowired
	private StayAvailDateRepository stayAvailDateRepository;

	@Test
	@Order(1)
	void stayAddTest() {
		long preCnt = stayRepository.count();

		// 지자체 대여 독립형
		stayRepository.saveAll(
			Stream.iterate(1, n -> n + 1)
				.limit(10)
				.map(n -> Stay.builder()
					.isHomestay(false)
					.title("별채 " + n + "호")
					.address("전남 해남 화산면 새꽃마을")
					.detailAddress("127-" + n)
					.capacity(5)
					.areaSize(40)
					.description("전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음")
					.build())
				.toList());

		assertEquals(preCnt + 10, stayRepository.count());
	}

	@Test
	@Order(2)
	void stayEditTest() {
		Stay target = Stay.builder()
			.isHomestay(false)
			.title("별채 0호")
			.address("전남 해남 화산면 새꽃마을")
			.detailAddress("127-51")
			.capacity(5)
			.areaSize(40)
			.description("전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음")
			.build();

		Stay savedStay = stayRepository.save(target);

		target.setCapacity(10);
		target.setAreaSize(50);
		target.setDescription("고양이들이 많음\n작은 텃밭 있음");
		stayRepository.save(target);

		Stay fetchedStay = stayRepository.findById(target.getId()).orElseThrow();

		assertEquals(10, savedStay.getCapacity());
		assertEquals(50, savedStay.getAreaSize());
		assertEquals(savedStay, fetchedStay);
	}

	@Test
	@Order(3)
	void stayAvailAddTest() {
		long preCount = stayAvailDateRepository.count();

		// 지자체 대여 독립형 5개
		List<Stay> stays = stayRepository.saveAll(
			IntStream.rangeClosed(1, 5)
				.mapToObj(n -> Stay.builder()
					.isHomestay(false)
					.title("별채 00" + n + "호")
					.address("전남 해남 화산면 새꽃마을")
					.detailAddress("127-00" + n)
					.capacity(5)
					.areaSize(40)
					.description("전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음")
					.build())
				.toList()
		);

		LocalDate today = LocalDate.now();

		// 각 별채 5일 오픈
		List<StayAvailDate> availDates =
			stays.stream()
				.flatMap(stay ->
					IntStream.range(0, 5)
						.mapToObj(d ->
							StayAvailDate.builder()
								.stay(stay)
								.availableDate(today.plusDays(d))
								.build()
						)
				)
				.toList();

		stayAvailDateRepository.saveAll(availDates);

		assertEquals(preCount + 5 * 5, stayAvailDateRepository.count());

	}
}
