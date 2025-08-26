package com.sido.backend.stay.service;

import org.springframework.stereotype.Service;

import com.sido.backend.member.entity.HostMember;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.stay.dto.StayDTO;
import com.sido.backend.stay.dto.StayRequestDTO;
import com.sido.backend.stay.dto.StayRegisterDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.repository.StayRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StayServiceImpl implements StayService {
	private final StayRepository stayRepository;
	private final HostMemberRepository hostMemberRepository;

	@Override
	public StayRegisterDTO addStay(long memberId, StayRequestDTO requestDTO) {
		HostMember host = hostMemberRepository.findById(memberId).orElseThrow();

		Stay stay = requestDTO.toEntity();
		stay.setHost(host);
		stayRepository.save(stay);

		return toRegisterDTO(stay);
	}

	@Override
	public StayDTO editStay(long stayId, long memberId, StayDTO stayDTO) {
		Stay stay = stayRepository.findById(stayId).orElseThrow();
		stay.setCapacity(stayDTO.getCapacity());
		stay.setAreaSize(stayDTO.getAreaSize());
		stay.setDescription(stayDTO.getDescription());

		return toEditDTO(stayRepository.save(stay));
	}

	private StayDTO toEditDTO(Stay stay) {
		return StayDTO.builder()
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.description(stay.getDescription())
			.build();
	}

	private StayRegisterDTO toRegisterDTO(Stay stay) {
		return StayRegisterDTO.builder()
			.title(stay.getTitle())
			.address(stay.getAddress())
			.capacity(stay.getCapacity())
			.areaSize(stay.getAreaSize())
			.ownerName(stay.getOwnerName())
			.ownerPhone(stay.getOwnerPhone())
			.description(stay.getDescription())
			.build();
	}
}
