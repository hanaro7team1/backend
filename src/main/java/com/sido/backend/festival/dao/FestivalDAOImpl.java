package com.sido.backend.festival.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sido.backend.festival.entity.Festival;
import com.sido.backend.festival.repository.FestivalRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FestivalDAOImpl implements FestivalDAO {
	private final FestivalRepository repository;

	@Override
	public List<Festival> findAll(Pageable pager) {
		return repository.findAll(pager).stream().toList();
	}

	@Override
	public Festival findOne(Long id) {
		return repository.findById(id).orElse(null);
	}
}
