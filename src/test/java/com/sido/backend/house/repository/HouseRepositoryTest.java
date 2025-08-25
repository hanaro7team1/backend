package com.sido.backend.house.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sido.backend.house.entity.House;

@SpringBootTest
class HouseRepositoryTest {
	@Autowired
	private HouseRepository houseRepository;

	@Test
	void houseAddTest() {
		short numOfHumans = 4;
		short size = 20;

		houseRepository.saveAll(
			Stream.iterate(1, n -> n + 1)
				.limit(10)
				.map(n -> House.builder()
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
	void houseEditTest() {
		short numOfHumansEdit = 5;
		short sizeEdit = 25;
		long id = 3L;

		House houseEdit = houseRepository.findById(id).orElseThrow();
		houseEdit.setAreaSize(sizeEdit);
		houseEdit.setCapacity(numOfHumansEdit);
		houseEdit.setDescription("고양이들이 많음\n작은 텃밭 있음");

		houseRepository.save(houseEdit);
	}
}
