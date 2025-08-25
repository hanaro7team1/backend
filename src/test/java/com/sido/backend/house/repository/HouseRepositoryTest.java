package com.sido.backend.house.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sido.backend.RepositoryTest;
import com.sido.backend.house.entity.House;
import com.sido.backend.house.entity.HouseAvailableDate;
import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;

class HouseRepositoryTest extends RepositoryTest {
	@Autowired
	HouseRepository houseRepository;

	@Autowired
	HouseAvailableDateRepository houseAvailableDateRepository;

	@Autowired
	HostMemberRepository hostMemberRepository;

	@Test
	@Order(1)
	void saveTest() {
		HostMember hm = hostMemberRepository.findByLoginId("host1").orElseThrow();
		House house = House.builder()
			.host(hm)
			.title(hm.getVillageName() + " 사랑방")
			.address(hm.getVillageName() + "-detail address")
			.areaSize(40)
			.capacity(5)
			.ownerPhone("010-3333-0000")
			.build();

		House savedHouse = houseRepository.save(house);
		House foundHouse = houseRepository.findById(savedHouse.getId()).orElseThrow();

		assertThat(foundHouse)
			.usingRecursiveComparison()
			.ignoringFields("createdAt", "updatedAt")
			.isEqualTo(savedHouse);
		assertEquals(savedHouse.getId(), foundHouse.getId());
	}

	@Test
	@Order(2)
	void addTest() {
		long preCnt = houseRepository.count();

		HostMember hm = hostMemberRepository.findByLoginId("host2").orElseThrow();
		List<House> houses = Stream.iterate(1, n -> n + 1).limit(5)
			.map(n -> House.builder()
				.host(hm)
				.title(hm.getVillageName() + " 사랑방 " + n + "호")
				.address(hm.getVillageName() + "-detail address" + n)
				.areaSize(40)
				.capacity(5)
				.ownerPhone("010-3333-000" + n)
				.description("description" + n)
				.build()
			).toList();

		houseRepository.saveAll(houses);

		assertEquals(preCnt + 5, houseRepository.count());
	}

	@Test
	@Order(3)
	void addDatesToHouseTest() {
		long prCnt = houseAvailableDateRepository.count();

		List<HouseAvailableDate> dates = LongStream.rangeClosed(1, 5)
			.boxed()
			.flatMap(houseId ->
				IntStream.range(0, 7)
					.mapToObj(d ->
						HouseAvailableDate.builder()
							.house(houseRepository.findById(houseId).orElseThrow())
							.date(LocalDate.now().plusDays(d))
							.build()
					)
			).toList();

		houseAvailableDateRepository.saveAll(dates);

		assertEquals(prCnt + 35, houseAvailableDateRepository.count());
	}
}
