package com.sido.backend.festival.repository;

import static org.junit.jupiter.api.Assertions.*;

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
		repository.saveAll(
			Stream.iterate(1, n -> n + 1)
				.limit(20)
				.map(n -> Festival.builder()
					.date("2025-10-" + n)
					.region("안동시")
					.title("축제" + n)
					.url("andong.com/festival=" + n)
					.description("festival description")
					.build())
				.toList());

		assertEquals(20, repository.count());
	}
}
