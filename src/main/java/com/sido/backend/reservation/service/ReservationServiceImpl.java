package com.sido.backend.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.common.exception.BadRequestException;
import com.sido.backend.common.exception.ConflictException;
import com.sido.backend.member.entity.Member;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.member.repository.MemberRepository;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.ReservationInfoDTO;
import com.sido.backend.reservation.dto.ReservationCommonDTOs.StaySummaryDTO;
import com.sido.backend.reservation.dto.ReservationConfirmRequestDTO;
import com.sido.backend.reservation.dto.ReservationConfirmResponseDTO;
import com.sido.backend.reservation.dto.ReservationCreateRequestDTO;
import com.sido.backend.reservation.dto.ReservationCreateResponseDTO;
import com.sido.backend.reservation.dto.ReservationDetailResponseDTO;
import com.sido.backend.reservation.dto.ReservationListFilter;
import com.sido.backend.reservation.dto.ReservationListItemDTO;
import com.sido.backend.reservation.dto.ReservationOverviewDTO;
import com.sido.backend.reservation.dto.ReservationViewStatus;
import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.entity.ReservationDay;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.entity.VisitStatus;
import com.sido.backend.reservation.repository.ReservationDayRepository;
import com.sido.backend.reservation.repository.ReservationQDslRepository;
import com.sido.backend.reservation.repository.ReservationRepository;
import com.sido.backend.reservation.repository.ReservationRepository.ReservationCounts;
import com.sido.backend.reservation.validation.AvailabilityChecker;
import com.sido.backend.reservation.validation.ReservationValidator;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.repository.StayRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final ReservationDayRepository reservationDayRepository;
	private final ReservationQDslRepository reservationQDslRepository;
	private final StayRepository stayRepository;
	private final MemberRepository memberRepository;
	private final HostMemberRepository hostMemberRepository;
	private final ReservationValidator reservationValidator;
	private final AvailabilityChecker availabilityChecker;

	@Override
	@Transactional
	public ReservationCreateResponseDTO createReservation(Long memberId, Long stayId,
		ReservationCreateRequestDTO createRequest) {
		Stay stay = stayRepository.findById(stayId).orElseThrow(
			() -> new EntityNotFoundException("해당 사랑방을 찾을 수 없습니다.")
		);
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.")
		);

		// 기본값
		LocalDate today = LocalDate.now();
		LocalDate startDate =
			(createRequest.startDate() == null) ? today : createRequest.startDate();
		LocalDate endDate =
			(createRequest.endDate() == null) ? today.plusDays(2) : createRequest.endDate();
		Integer personCnt =
			(createRequest.personCnt() == null) ? 2 : createRequest.personCnt();

		reservationValidator.assertCoreRules(stay, startDate, endDate, personCnt);
		availabilityChecker.assertAllDatesAvailable(stayId, startDate, endDate);

		Reservation reservation = new Reservation();
		reservation.setStay(stay);
		reservation.setMember(member);
		reservation.setResrvStatus(ResrvStatus.PENDING);
		reservation.setStartDate(startDate);
		reservation.setEndDate(endDate);
		reservation.setPersonCnt(personCnt);

		reservationRepository.save(reservation);

		// TODO 배치/스케줄링-> PENDING 5분 or 10분 후 예약 삭제 or CANCELLED

		return toCreateResponseDTO(reservation);
	}

	@Override
	@Transactional
	public ReservationConfirmResponseDTO confirmReservation(Long memberId, Long reservationId,
		ReservationConfirmRequestDTO confirmRequest) {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
			() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다.")
		);

		reservationValidator.assertOwnedBy(reservation, memberId); // 본인 예약 검증

		// 멱등성: 이미 예약됐으면 현재 상태 그대로 반환
		if (reservation.getResrvStatus() == ResrvStatus.RESERVED) {
			return toConfirmResponseDTO(reservation);
		}

		reservationValidator.assertConfirmable(reservation); // PENDING인지 검증

		if (confirmRequest.reservationInfo().isFarm() == null) {
			throw new BadRequestException("농장 체험 유무를 선택해야 합니다.");
		}

		// 엔티티의 현재 값으로 기본 세팅
		LocalDate newStart = reservation.getStartDate();
		LocalDate newEnd = reservation.getEndDate();
		Integer newCnt = reservation.getPersonCnt();

		reservationValidator.assertDatesPairOrNone(confirmRequest.reservationInfo().startDate(),
			confirmRequest.reservationInfo().endDate()); // startDate, endDate 둘다 있거나 둘다 없거나

		if (confirmRequest.reservationInfo().startDate() != null) { // 날짜 들어왔으면 변경사항으로 덮어쓰기
			newStart = confirmRequest.reservationInfo().startDate();
			newEnd = confirmRequest.reservationInfo().endDate();
		}
		if (confirmRequest.reservationInfo().personCnt() != null) { // 인원수 들어왔으면 변경사항으로 덮어쓰기
			newCnt = confirmRequest.reservationInfo().personCnt();
		}

		// 예약 요청 검증
		reservationValidator.assertCoreRules(reservation.getStay(), newStart, newEnd, newCnt);
		availabilityChecker.assertAllDatesAvailable(reservation.getStay().getId(), newStart, newEnd);

		// 엔티티에 반영
		reservation.setStartDate(newStart);
		reservation.setEndDate(newEnd);
		reservation.setPersonCnt(newCnt);
		reservation.setIsFarm(confirmRequest.reservationInfo().isFarm());

		List<ReservationDay> reservationDays = new ArrayList<>();
		for (LocalDate d = newStart; d.isBefore(newEnd); d = d.plusDays(1)) {
			ReservationDay day = ReservationDay.builder()
				.date(d)
				.reservation(reservation)
				.stay(reservation.getStay())
				.build();
			reservationDays.add(day);
		}

		try {
			reservationDayRepository.saveAll(reservationDays);
		} catch (DataIntegrityViolationException e) { // 409 CONFLICT
			throw new ConflictException("다른 사용자가 먼저 예약을 확정했습니다.");
		}

		reservation.setResrvStatus(ResrvStatus.RESERVED);
		reservation.setReservedAt(LocalDateTime.now());

		reservationRepository.save(reservation);

		return toConfirmResponseDTO(reservation);
	}

	@Override
	public ReservationDetailResponseDTO getReservationDetail(Long memberId, Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
			() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다.")
		);

		reservationValidator.assertOwnedBy(reservation, memberId); // 본인 예약만 확인 가능

		reservationValidator.assertNotPending(reservation, "예약 상세 조회");

		return toDetailResponseDTO(reservation);
	}

	@Override
	@Transactional
	public void cancelReservation(Long memberId, Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(
			() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다.")
		);

		reservationValidator.assertOwnedBy(reservation, memberId); // 본인 예약 검증

		// 멱등성
		if (reservation.getResrvStatus() == ResrvStatus.CANCELLED) {
			return;
		}

		reservation.setResrvStatus(ResrvStatus.CANCELLED); // 예약 취소 상태로
		reservation.setVisitStatus(null); // 방문 상태 null로
		reservationRepository.save(reservation);

		reservationDayRepository.deleteByReservationId(reservationId); // ReservationDay 날짜 점유 해제
	}

	@Override
	public ReservationOverviewDTO getReservationOverview(Long memberId) {
		hostMemberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 호스트를 찾을 수 없습니다.")
		);

		ReservationCounts resrvCnt = reservationRepository.summarizeByHost(memberId);

		return ReservationOverviewDTO.builder()
			.upcomingCnt(resrvCnt.getUpcomingCnt())
			.inProgressCnt(resrvCnt.getInProgressCnt())
			.completedCnt(resrvCnt.getCompletedCnt())
			.build();
	}

	@Override
	public PageResponseDTO<ReservationListItemDTO, Reservation> getReservationList(Long memberId, int page,
		int listSize, ReservationListFilter filter) {
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.")
		);

		Pageable pageable = PageRequest.of(page - 1, listSize);
		Slice<Reservation> reservationSlice = reservationQDslRepository.findList(memberId, filter, pageable);

		return new PageResponseDTO<>(reservationSlice, this::toListItemDTO);
	}

	@Override
	public ReservationListItemDTO getNextReservation(Long memberId) {
		memberRepository.findById(memberId).orElseThrow(
			() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다.")
		);

		Pageable pageable = PageRequest.of(0, 1);

		Slice<Reservation> reservationSlice = reservationQDslRepository.findList(memberId,
			ReservationListFilter.RESERVED, pageable);

		Reservation nextReservation = reservationSlice.getContent().isEmpty()
			? null
			: reservationSlice.getContent().getFirst();

		if (nextReservation == null) {
			return null;
		}

		return toListItemDTO(nextReservation);
	}

	private ReservationCreateResponseDTO toCreateResponseDTO(Reservation reservation) {
		return new ReservationCreateResponseDTO(
			reservation.getId(),
			StaySummaryDTO.ofBase(reservation.getStay()),
			ReservationInfoDTO.ofDatesGuest(reservation.getStartDate(), reservation.getEndDate(),
				reservation.getPersonCnt()),
			reservation.getResrvStatus()
		);
	}

	private ReservationConfirmResponseDTO toConfirmResponseDTO(Reservation reservation) {
		LocalDate today = LocalDate.now();
		boolean inRange = !today.isBefore(reservation.getStartDate()) && !today.isAfter(reservation.getEndDate());
		long dDay = ChronoUnit.DAYS.between(today, reservation.getStartDate());

		// TODO 배치/스케줄링으로 VisitStatus 업데이트
		if (inRange) { // [start, end]
			reservation.setVisitStatus(VisitStatus.IN_PROGRESS);
		} else if (dDay > 0) {
			reservation.setVisitStatus(VisitStatus.UPCOMING);
		} else if (dDay < 0) {
			reservation.setVisitStatus(VisitStatus.COMPLETED);
		}
		reservationRepository.save(reservation);

		return new ReservationConfirmResponseDTO(
			reservation.getId(),
			reservation.getStay().getId(),
			reservation.getResrvStatus(),
			reservation.getVisitStatus(),
			dDay,
			reservation.getReservedAt()
		);
	}

	private ReservationDetailResponseDTO toDetailResponseDTO(Reservation reservation) {
		return new ReservationDetailResponseDTO(
			reservation.getId(),
			reservation.getResrvStatus(),
			reservation.getMember().getName(),
			reservation.getMember().getPhone(),
			reservation.getStay().getIsHomestay(),
			reservation.getStay().getHostName(),
			reservation.getStay().getHostPhone(),
			StaySummaryDTO.ofFull(reservation.getStay()),
			ReservationInfoDTO.ofAll(
				reservation.getStartDate(), reservation.getEndDate(), reservation.getPersonCnt(),
				reservation.getIsFarm()
			)
		);
	}

	private ReservationListItemDTO toListItemDTO(Reservation reservation) {
		LocalDate today = LocalDate.now();
		boolean inRange = !today.isBefore(reservation.getStartDate()) && !today.isAfter(reservation.getEndDate());
		long dDay = ChronoUnit.DAYS.between(today, reservation.getStartDate());

		// TODO 배치/스케줄링으로 VisitStatus 업데이트
		if (reservation.getResrvStatus() == ResrvStatus.RESERVED) {
			if (inRange) { // [start, end]
				reservation.setVisitStatus(VisitStatus.IN_PROGRESS);
			} else if (dDay > 0) {
				reservation.setVisitStatus(VisitStatus.UPCOMING);
			} else if (dDay < 0) {
				reservation.setVisitStatus(VisitStatus.COMPLETED);
			}
		}
		reservationRepository.save(reservation);

		return new ReservationListItemDTO(
			reservation.getId(),
			reservation.getStay().getTitle(),
			ReservationViewStatus.from(reservation.getResrvStatus(), reservation.getVisitStatus()),
			dDay,
			ReservationInfoDTO.ofDates(reservation.getStartDate(), reservation.getEndDate())
		);
	}

}
