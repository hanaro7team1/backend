package com.sido.backend.festival.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sido.backend.festival.entity.Festival;

public interface FestivalDAO {
	List<Festival> findAll(Pageable pager);

	Festival findOne(Long id);
}
