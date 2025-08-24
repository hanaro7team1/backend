package com.sido.backend.festival.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.festival.dao.FestivalDAO;
import com.sido.backend.festival.dto.FestivalResponseDTO;
import com.sido.backend.festival.dto.FestivalResponseDetailDTO;
import com.sido.backend.festival.dto.FestivalRequestDTO;
import com.sido.backend.festival.entity.Festival;
import com.sido.backend.festival.repository.FestivalRepository;

@Service
public class FestivalServiceImpl implements FestivalService{
	private final FestivalRepository repository;
	// private final FestivalDAO dao;

	public FestivalServiceImpl(FestivalRepository repository, FestivalDAO dao) {
		this.repository = repository;
		// this.dao = dao;
	}

	@Override
	public List<FestivalResponseDTO> getFestivalList(int page, int listSize) {
		Slice<Festival> lists = repository.findAll(
			PageRequest.of(page - 1, listSize, Sort.by(Sort.Order.desc("id"))));
		return new PageResponseDTO<>(lists, FestivalServiceImpl::toDTO).getDtoList();
	}

	@Override
	public FestivalResponseDetailDTO getServiceDetail(Long id) {
		return repository.findById(id).map(FestivalServiceImpl::toDetailDTO).orElse(null);
	}

	@Override
	public FestivalResponseDetailDTO addFestival(FestivalRequestDTO requestDTO) {
		Festival festival = toEntity(requestDTO);

		return toDetailDTO(repository.save(festival));
	}

	@Override
	public FestivalResponseDetailDTO editFestival(FestivalRequestDTO requestDTO) {
		Festival festival = repository.findById(requestDTO.getId()).orElseThrow();
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

	public static FestivalResponseDTO toDTO (Festival festival) {
		return FestivalResponseDTO.builder()
			.id(festival.getId())
			.title(festival.getTitle())
			.startDate(festival.getStartDate())
			.endDate(festival.getEndDate())
			.city(festival.getCity())
			.build();
	}

	public static FestivalResponseDetailDTO toDetailDTO (Festival festival) {
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

	public static Festival toEntity (FestivalRequestDTO dto) {
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
