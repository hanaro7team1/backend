package com.sido.backend.festival.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.YearMonth;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.sido.backend.festival.entity.Festival;

@Rollback(false)
class FestivalRepositoryTest extends RepositoryTest{
	@Autowired
	FestivalRepository repository;

	@Test
	void addTest() {
		YearMonth ym = YearMonth.of(2025, 10);

		repository.saveAll(
			Stream.iterate(1, n -> n + 1)
				.limit(20)
				.map(n -> Festival.builder()
					.title("축제" + n)
					.startDate(ym.atDay(n))
					.endDate(ym.atDay(n).plusDays(14))
					.city("안동시")
					.street("어딘가로 " + n)
					.price(10000)
					.url("andong.com/festival=" + n)
					.description("festival description")
					.build())
				.toList());

		assertEquals(20, repository.count());
	}

	@Test
	void editTest() {
		long before = repository.count();

		Festival target = repository.findAll().stream()
			.filter(f -> f.getTitle().equals("축제10"))
			.findFirst().orElseThrow();

		target.setTitle("축제10-수정");
		target.setCity("포항시");
		repository.save(target);

		Festival updated = repository.findById(target.getId()).orElseThrow();
		assertEquals(before, repository.count());
		assertEquals("축제10-수정", updated.getTitle());
		assertEquals("포항시", updated.getCity());
	}

	@Test
	void deleteTest() {
		long id = 3L;
		repository.deleteById(id);

		assertTrue(repository.findById(id).isEmpty());
	}
}
