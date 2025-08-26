package com.sido.backend.reservation.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.sido.backend.member.entity.Member;
import com.sido.backend.member.repository.MemberRepository;
import com.sido.backend.reservation.dto.ReservationDTO;
import com.sido.backend.reservation.dto.ReservationResponseDTO;
import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.repository.ReservationRepository;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.repository.StayAvailDateRepository;
import com.sido.backend.stay.repository.StayRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final StayRepository stayRepository;
	private final StayAvailDateRepository stayAvailDateRepository;
	private final MemberRepository memberRepository;

	@Override
	public ReservationResponseDTO createReservation(long stayId, Long memberId, ReservationDTO reservationDTO) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 회원을 찾을 수 없습니다.")
		);

		// 숙박 예약 일정 없을 시, 기본 값
		LocalDate today = LocalDate.now();
		System.out.println("today = " + today);
		System.out.println("today.minusDays(1) = " + today.minusDays(1));
		LocalDate startDate = (reservationDTO.getStartDate() == null) ? today : reservationDTO.getStartDate();
		LocalDate endDate = (reservationDTO.getEndDate() == null) ? today.plusDays(1) : reservationDTO.getEndDate();

		if (endDate.isBefore(startDate)) { // 당일치기는...??
			throw new IllegalArgumentException("종료일은 시작일보다 앞설 수 없습니다.");
		}

		if (!startDate.isAfter(today.minusDays(1))) {
			throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
		}

		if (!stay.getIsActive()) {
			throw new IllegalArgumentException("해당 사랑방은 예약이 닫혔습니다.");
		}

		if (reservationDTO.getPersonCnt() > stay.getCapacity()) {
			throw new IllegalArgumentException("예약 가능한 인원수를 초과하였습니다.");
		}

		long requestDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
		long availDaysCnt = stayAvailDateRepository.countByStayIdAndAvailableDateBetweenAndIsAvailableTrue(stayId,
			startDate,
			endDate);
		System.out.println("requestDays = " + requestDays);
		System.out.println("availDaysCnt = " + availDaysCnt);
		if (requestDays != availDaysCnt) {
			throw new IllegalArgumentException("선택한 기간에 예약 불가 날짜가 포함되어 있습니다.");
		}
		
		/*
		TODO
		배치/스케줄링
		PENDING 후 얼마 동안의 시간이 지난 후 PENDING을 풀지 체크. 예약 삭제? or CANCELED?
		오늘 날짜 지나면 StayAvailDate isAvailable=false로
		 */

		Reservation reservation = new Reservation();
		reservation.setStay(stay);
		reservation.setMember(member);
		reservation.setResrvStatus(ResrvStatus.PENDING);
		reservation.setStartDate(startDate);
		reservation.setEndDate(endDate);
		reservation.setPersonCnt(reservationDTO.getPersonCnt());
		reservation.setIsFarm(reservationDTO.getIsFarm());

		reservationRepository.save(reservation);

		return toReservationResponseDTO(reservation);
	}

	@Override
	public ReservationResponseDTO confirmReservation(long stayId, Long memberId) {
		/*
		TODO
		멤버 비교해보고 일치 안 하면 exception
		ResrvStatus = RESERVED
		reservedAt = now()
		StayAvailDate isAvailable=false로
		 */
		return null;
	}

	private ReservationResponseDTO toReservationResponseDTO(Reservation reservation) {
		return ReservationResponseDTO.builder()
			.reservationId(reservation.getId())
			.stayId(reservation.getStay().getId())
			.resrvStatus(reservation.getResrvStatus())
			.startDate(reservation.getStartDate())
			.endDate(reservation.getEndDate())
			.personCnt(reservation.getPersonCnt())
			.isFarm(reservation.getIsFarm())
			.reservedAt(reservation.getReservedAt())
			.build();
	}
}
