package com.sido.backend.reservation.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sido.backend.RepositoryTest;
import com.sido.backend.member.entity.Member;
import com.sido.backend.member.entity.MemberRole;
import com.sido.backend.member.repository.HostMemberRepository;
import com.sido.backend.member.repository.MemberRepository;
import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.entity.VisitStatus;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.entity.StayAvailDate;
import com.sido.backend.stay.repository.StayAvailDateRepository;
import com.sido.backend.stay.repository.StayRepository;

class ReservationRepositoryTest extends RepositoryTest {
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private StayRepository stayRepository;
	@Autowired
	private StayAvailDateRepository stayAvailDateRepository;
	@Autowired
	private HostMemberRepository hostMemberRepository;
	@Autowired
	private MemberRepository memberRepository;

	private Stay stay;
	private StayAvailDate stayAvailDate;
	private Member user;

	private Reservation upComming;
	private Reservation inProgress;
	private Reservation completed;

	@BeforeEach
	void setUp() {
		user = Member.builder()
			.loginId("host123")
			.password("@@##asdasd3434lslslslmfm2mfs33$123s5ssaaa")
			.name("User")
			.role(MemberRole.ROLE_USER)
			.phone("010-1234-1234")
			.build();
		user = memberRepository.save(user);

		stay = Stay.builder()
			.isHomestay(Boolean.FALSE)
			.title("place123")
			.address("addr123")
			.detailAddress("detail palce")
			.capacity(3)
			.areaSize(30)
			.description("description")
			.build();
		stay = stayRepository.save(stay);

		LocalDate today = LocalDate.now();
		LocalDateTime reserved = today.atStartOfDay();

		stayAvailDate = StayAvailDate.builder()
			.stay(stay)
			.availableDate(today.plusDays(30))
			.build();
		stayAvailDate = stayAvailDateRepository.save(stayAvailDate);

		upComming = new Reservation();
		upComming.setStartDate(today.plusDays(5));
		upComming.setEndDate(today.plusDays(7));
		upComming.setPersonCnt(3);
		upComming.setIsFarm(Boolean.FALSE);
		upComming.setResrvStatus(ResrvStatus.RESERVED);
		upComming.setVisitStatus(VisitStatus.UPCOMING);
		upComming.setMember(user);
		upComming.setStay(stay);
		upComming.setReservedAt(reserved);
		upComming = reservationRepository.save(upComming);

		inProgress = new Reservation();
		inProgress.setStartDate(today);
		inProgress.setEndDate(today.plusDays(2));
		inProgress.setPersonCnt(3);
		inProgress.setIsFarm(Boolean.FALSE);
		inProgress.setResrvStatus(ResrvStatus.RESERVED);
		inProgress.setVisitStatus(VisitStatus.IN_PROGRESS);
		inProgress.setMember(user);
		inProgress.setStay(stay);
		inProgress.setReservedAt(reserved);
		inProgress = reservationRepository.save(inProgress);

		completed = new Reservation();
		completed.setStartDate(today.minusDays(10));
		completed.setEndDate(today.minusDays(5));
		completed.setPersonCnt(3);
		completed.setIsFarm(Boolean.FALSE);
		completed.setResrvStatus(ResrvStatus.RESERVED);
		completed.setVisitStatus(VisitStatus.COMPLETED);
		completed.setMember(user);
		completed.setStay(stay);
		completed.setReservedAt(reserved);
		completed = reservationRepository.save(completed);
	}

	@Test
	void checkReservation() {
		assertThat(reservationRepository.count()).isEqualTo(30);
	}

	@Test
	void checkUpComming() {
		boolean exists = reservationRepository.existsUpcomingByStay(stay.getId());
		assertThat(exists).isTrue();
	}

	@Test
	void cancelReservation() {
		assertThat(reservationRepository.existsUpcomingByStay(stay.getId())).isTrue();

		Reservation toCancel = reservationRepository.findById(upComming.getId()).orElseThrow();
		toCancel.setResrvStatus(ResrvStatus.CANCELLED);
		toCancel.setVisitStatus(null);
		reservationRepository.save(toCancel);

		Reservation cancelled = reservationRepository.findById(upComming.getId()).orElseThrow();
		assertThat(cancelled.getResrvStatus()).isEqualTo(ResrvStatus.CANCELLED);
		assertThat(cancelled.getVisitStatus()).isNull();
		
		boolean existsUpcoming = reservationRepository.existsUpcomingByStay(stay.getId());
		assertThat(existsUpcoming).isFalse();
	}
}
