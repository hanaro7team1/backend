package com.sido.backend.festival.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sido.backend.RepositoryTest;
import com.sido.backend.festival.entity.Festival;

class FestivalRepositoryTest extends RepositoryTest {
	@Autowired
	FestivalRepository festivalRepository;

	@Test
	@Order(1)
	void addTest() {
		long preCount = festivalRepository.count();
		YearMonth ym = YearMonth.of(2025, 10);

		festivalRepository.saveAll(
			Stream.iterate(1, n -> n + 1)
				.limit(20)
				.map(n -> Festival.builder()
					.title("축제" + n)
					.startDate(ym.atDay(n))
					.endDate(ym.atDay(n).plusDays(14))
					.city("안동시")
					.location("어딘가로 " + n)
					.price(10000)
					.url("andong.com/festival=" + n)
					.description("festival description")
					.build())
				.toList());

		assertEquals(preCount + 20, festivalRepository.count());
	}

	@Test
	@Order(2)
	void editTest() {
		long id = festivalRepository.count();

		LocalDate date = LocalDate.of(2025, 10, 14);
		Festival target = Festival.builder()
			.title("수정할 축제")
			.startDate(date)
			.endDate(date.plusDays(14))
			.city("안동시")
			.location("어딘가로 " + id + 1)
			.price(10000)
			.url("andong.com/festival=" + id + 1)
			.description("festival description to be edited")
			.build();
		festivalRepository.save(target);

		target.setTitle("수정 완료 축제");
		target.setCity("포항시");
		festivalRepository.save(target);

		Festival updated = festivalRepository.findById(target.getId()).orElseThrow();
		assertEquals("수정 완료 축제", updated.getTitle());
		assertEquals("포항시", updated.getCity());
	}

	@Test
	@Order(3)
	void deleteTest() {
		long id = 3L;
		festivalRepository.deleteById(id);

		assertTrue(festivalRepository.findById(id).isEmpty());
	}
}
