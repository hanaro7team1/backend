package com.sido.backend.festival.service;

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

@Service
public class FestivalServiceImpl implements FestivalService {
	private final FestivalRepository repository;

	public FestivalServiceImpl(FestivalRepository repository) {
		this.repository = repository;
	}

	@Override
	public PageResponseDTO<FestivalDTO, Festival> getFestivalList(int page, int listSize) {
		Slice<Festival> lists = repository.findAll(
			PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id"))));
		return new PageResponseDTO<>(lists, FestivalServiceImpl::toDTO);
	}

	@Override
	public FestivalResponseDetailDTO getServiceDetail(Long id) {
		return repository.findById(id).map(FestivalServiceImpl::toDetailDTO).orElseThrow(
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

	public static FestivalDTO toDTO(Festival festival) {
		return FestivalDTO.builder()
			.id(festival.getId())
			.title(festival.getTitle())
			.startDate(festival.getStartDate())
			.endDate(festival.getEndDate())
			.city(festival.getCity())
			.build();
	}

	public static FestivalResponseDetailDTO toDetailDTO(Festival festival) {
		return FestivalResponseDetailDTO.builder()
			.id(festival.getId())
			.title(festival.getTitle())
			.startDate(festival.getStartDate())
			.endDate(festival.getEndDate())
			.city(festival.getCity())
			.street(festival.getStreet())
			.price(festival.getPrice())
			.url(festival.getUrl())
			.description(festival.getDescription())
			.createdAt(festival.getCreatedAt())
			.updatedAt(festival.getUpdatedAt())
			.build();
	}

	public static Festival toEntity(FestivalRequestDTO dto) {
		return Festival.builder()
			.title(dto.getTitle())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.city(dto.getCity())
			.street(dto.getStreet())
			.price(dto.getPrice())
			.url(dto.getUrl())
			.description(dto.getDescription())
			.build();
	}
}
