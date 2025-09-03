package com.sido.backend.reservation.scheduler;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sido.backend.reservation.repository.ReservationQDslRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class VisitStatusScheduler {
	private final ReservationQDslRepository reservationQDslRepository;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void updateVisitStatusDaily() {
		LocalDate todayKst = LocalDate.now(ZoneId.of("Asia/Seoul"));
		long updatedCnt = reservationQDslRepository.bulkUpdateVisitStatus(todayKst);
		log.info("Updated VisitStatusDaily = {}", updatedCnt);
	}
}
