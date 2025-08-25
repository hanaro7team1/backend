package com.sido.backend.stay.repository;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sido.backend.stay.entity.Stay;

@SpringBootTest
class StayRepositoryTest {
	@Autowired
	private StayRepository stayRepository;

	@Test
	void stayAddTest() {
		short numOfHumans = 4;
		short size = 20;

		stayRepository.saveAll(
			Stream.iterate(1, n -> n + 1)
				.limit(10)
				.map(n -> Stay.builder()
					.isHomestay(true)
					.title("사랑방 " + n + "호")
					.address("전남 해남 화산면 새꽃마을")
					.capacity(numOfHumans)
					.areaSize(size)
					.owner("박춘갑")
					.ownerPhone("010-1234-1234")
					.description("전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음")
					.build())
				.toList());
	}

	@Test
	void stayEditTest() {
		short numOfHumansEdit = 5;
		short sizeEdit = 25;
		long id = 3L;

		Stay stayEdit = stayRepository.findById(id).orElseThrow();
		stayEdit.setAreaSize(sizeEdit);
		stayEdit.setCapacity(numOfHumansEdit);
		stayEdit.setDescription("고양이들이 많음\n작은 텃밭 있음");

		stayRepository.save(stayEdit);
	}
}
