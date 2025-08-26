package com.sido.backend.stay.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.entity.StayAvailDate;

@SpringBootTest
class StayRepositoryTest {
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
					.title("사랑방 " + n + "호")
					.address("전남 해남 화산면 새꽃마을")
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
		long id = 3L;

		Stay stayEdit = stayRepository.findById(id).orElseThrow();
		stayEdit.setCapacity(10);
		stayEdit.setAreaSize(50);
		stayEdit.setDescription("고양이들이 많음\n작은 텃밭 있음");

		Stay savedStay = stayRepository.save(stayEdit);
		assertEquals(10, savedStay.getCapacity());
		assertEquals(50, savedStay.getAreaSize());
	}

	@Test
	@Order(3)
	void stayAvailAddTest() {
		long preCnt = stayAvailDateRepository.count();

		List<StayAvailDate> fiveDays = LongStream.rangeClosed(1, 5)
			.boxed()
			.flatMap(stayId ->
				IntStream.range(0, 5)
					.mapToObj(d ->
						StayAvailDate.builder()
							.availableDate(LocalDate.now().plusDays(d))
							.stay(stayRepository.findById(stayId).orElseThrow())
							.build()
					)
			).toList();

		List<StayAvailDate> threeDays = LongStream.rangeClosed(1, 5)
			.boxed()
			.flatMap(stayId ->
				IntStream.range(0, 3)
					.mapToObj(d ->
						StayAvailDate.builder()
							.availableDate(LocalDate.now().plusDays(10 + d))
							.stay(stayRepository.findById(stayId).orElseThrow())
							.build()
					)
			).toList();

		stayAvailDateRepository.saveAll(fiveDays);
		stayAvailDateRepository.saveAll(threeDays);

		assertEquals(preCnt + 25 + 15, stayAvailDateRepository.count());
	}
}
