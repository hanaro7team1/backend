package com.sido.backend.common.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Slice;

import lombok.Getter;

@Getter
public class PageResponseDTO<DTO, ENTITY> {
	private final List<DTO> dtoList;
	private final boolean hasNext;

	public PageResponseDTO(Slice<ENTITY> results, Function<ENTITY, DTO> fn) {
		this.dtoList = results.stream().map(fn).collect(Collectors.toList());
		this.hasNext = results.hasNext();
	}
}
