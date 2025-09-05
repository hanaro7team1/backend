package com.sido.backend.realestate.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sido.backend.RepositoryTest;
import com.sido.backend.realestate.entity.RealEstate;
import com.sido.backend.realestate.entity.RealEstateImage;

class RealEstatesRepositoryTest extends RepositoryTest {

	@Autowired
	RealEstateRepository realEstatesRepository;

	@Autowired
	RealEstateImageRepository realEstateImageRepository;

	@Test
	@Order(1)
	void saveRealEstatesTest() {
		RealEstate realEstate = RealEstate.builder()
			.location("서울시 강남구 테헤란로 123")
			.price(1000000000)
			.capacity(6)
			.area(100)
			.description("강남역 근처 아파트")
			.areaSize(84.5)
			.tradeType("매매")
			.roomCount(3)
			.house("아파트")
			.build();

		// 부동산 이미지 추가
		List<RealEstateImage> images = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			RealEstateImage image = RealEstateImage.builder()
				.realEstate(realEstate)
				.build();
			images.add(image);
		}
		realEstate.setImages(images);

		RealEstate savedRealEstate = realEstatesRepository.save(realEstate);
		RealEstate fetchedRealEstate = realEstatesRepository.findById(savedRealEstate.getId()).orElseThrow();

		assertEquals(savedRealEstate.getLocation(), fetchedRealEstate.getLocation());
		assertNotNull(fetchedRealEstate.getId());
		assertEquals(2, fetchedRealEstate.getImages().size());
		assertNotNull(fetchedRealEstate.getImages().get(0).getId());
	}

	@Test
	@Order(2)
	void addMultipleRealEstatesTest() {
		long initialCount = realEstatesRepository.count();

		Random random = new Random();
		List<RealEstate> realEstatesList = IntStream.range(0, 5)
			.mapToObj(i -> {
				int num = random.nextInt(1000) + 1;
				RealEstate realEstate = RealEstate.builder()
					.location("서울시 송파구 올림픽로 " + num)
					.price(500000000 + num * 10000000)
					.capacity(2 + num)
					.area(50 + num * 5)
					.description("잠실 근처 오피스텔 " + (i + 1))
					.areaSize(40.0 + num * 2)
					.tradeType(num % 2 == 0 ? "전세" : "매매")
					.roomCount(1 + num % 2)
					.house("오피스텔")
					.build();

				List<RealEstateImage> images = IntStream.range(0, 3)
					.mapToObj(j -> RealEstateImage.builder()
						.realEstate(realEstate)
						.build())
					.collect(Collectors.toList());
				realEstate.setImages(images);
				return realEstate;
			})
			.collect(Collectors.toList());

		realEstatesRepository.saveAll(realEstatesList);

		assertEquals(initialCount + 5, realEstatesRepository.count());
	}
}
