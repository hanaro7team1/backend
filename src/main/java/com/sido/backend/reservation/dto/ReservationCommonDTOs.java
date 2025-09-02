package com.sido.backend.reservation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.entity.VisitStatus;
import com.sido.backend.stay.entity.Stay;

import jakarta.validation.constraints.NotNull;

public class ReservationCommonDTOs {
	private static String combineAddress(String address, String detailAddress) {
		String base = address == null ? "" : address.trim();
		String detail = (detailAddress == null || detailAddress.isBlank()) ? "" : detailAddress.trim();
		return (base + " " + detail).trim();
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record StaySummaryDTO(Long stayId, String title, String address) {
		public static StaySummaryDTO ofBase(Stay stay) {
			return new StaySummaryDTO(stay.getId(), stay.getTitle(), stay.getAddress());
		}

		public static StaySummaryDTO ofFull(Stay stay) {
			return new StaySummaryDTO(stay.getId(), stay.getTitle(),
				combineAddress(stay.getAddress(), stay.getDetailAddress()));
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL) // null은 숨김
	public record ReservationInfoDTO(
		LocalDate startDate, LocalDate endDate, Integer personCnt, @NotNull Boolean isFarm) {
		public static ReservationInfoDTO ofDates(LocalDate start, LocalDate end) {
			return new ReservationInfoDTO(start, end, null, null);
		}

		public static ReservationInfoDTO ofDatesGuest(LocalDate start, LocalDate end, Integer cnt) {
			return new ReservationInfoDTO(start, end, cnt, null);
		}

		public static ReservationInfoDTO ofAll(LocalDate start, LocalDate end, Integer cnt, Boolean farm) {
			return new ReservationInfoDTO(start, end, cnt, farm);
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record ReservationStatusDTO(
		ResrvStatus resrvStatus, VisitStatus visitStatus, Long dDay, LocalDateTime reservedAt) {
		public static ReservationStatusDTO summary(ResrvStatus resrv, VisitStatus visit, Long dDay) {
			return new ReservationStatusDTO(resrv, visit, dDay, null);
		}

		public static ReservationStatusDTO detail(ResrvStatus resrv, VisitStatus visit, Long dDay, LocalDateTime at) {
			return new ReservationStatusDTO(resrv, visit, dDay, at);
		}
	}
}
