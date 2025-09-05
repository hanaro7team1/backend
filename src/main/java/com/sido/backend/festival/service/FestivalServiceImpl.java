package com.sido.backend.festival.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.festival.dto.FestivalDTO;
import com.sido.backend.festival.dto.FestivalRequestDTO;
import com.sido.backend.festival.dto.FestivalResponseDetailDTO;
import com.sido.backend.festival.entity.Festival;
import com.sido.backend.festival.repository.FestivalRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FestivalServiceImpl implements FestivalService {
	private final FestivalRepository repository;

	@Value("${app.s3.publicBaseUrl}")
	private String publicBaseUrl;

	@Override
	public PageResponseDTO<FestivalDTO, Festival> getFestivalList(int page, int listSize) {
		Slice<Festival> lists = repository.findAll(
			PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id"))));
		return new PageResponseDTO<>(lists, this::toDTO);
	}

	@Override
	public FestivalResponseDetailDTO getServiceDetail(Long id) {
		return repository.findById(id).map(this::toDetailDTO).orElseThrow(
			() -> new EntityNotFoundException("해당 축제를 찾을 수 없습니다.")
		);
	}

	@Override
	public FestivalResponseDetailDTO addFestival(FestivalRequestDTO requestDTO) {
		Festival festival = toEntity(requestDTO);

		return toDetailDTO(repository.save(festival));
	}

	@Override
	public FestivalResponseDetailDTO editFestival(FestivalRequestDTO requestDTO) {
		Festival festival = repository.findById(requestDTO.getId()).orElseThrow(
			() -> new EntityNotFoundException("해당 축제를 찾을 수 없습니다.")
		);
		festival.setStartDate(requestDTO.getStartDate());
		festival.setEndDate(requestDTO.getEndDate());
		festival.setPrice(requestDTO.getPrice());
		festival.setUrl(requestDTO.getUrl());
		festival.setDescription(requestDTO.getDescription());

		return toDetailDTO(repository.save(festival));
	}

	@Override
	public void removeFestival(Long id) {
		repository.deleteById(id);
	}

	private FestivalDTO toDTO(Festival festival) {
		return FestivalDTO.builder()
			.id(festival.getId())
			.title(festival.getTitle())
			.startDate(festival.getStartDate())
			.endDate(festival.getEndDate())
			.city(festival.getCity())
			.imageUrl(publicBaseUrl + "/" + festival.getImages().getFirst().getS3Key())
			.build();
	}

	private FestivalResponseDetailDTO toDetailDTO(Festival festival) {
		return FestivalResponseDetailDTO.builder()
			.id(festival.getId())
			.title(festival.getTitle())
			.startDate(festival.getStartDate())
			.endDate(festival.getEndDate())
			.city(festival.getCity())
			.location(festival.getLocation())
			.price(festival.getPrice())
			.url(festival.getUrl())
			.description(festival.getDescription())
			.imageUrl(publicBaseUrl + "/" + festival.getImages().getFirst().getS3Key())
			.build();
	}

	private Festival toEntity(FestivalRequestDTO dto) {
		return Festival.builder()
			.title(dto.getTitle())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.city(dto.getCity())
			.location(dto.getLocation())
			.price(dto.getPrice())
			.url(dto.getUrl())
			.description(dto.getDescription())
			.build();
	}
}
