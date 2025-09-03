package com.sido.backend.reservation.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sido.backend.stay.entity.Stay;

import jakarta.validation.constraints.NotNull;

public class ReservationCommonDTOs {
	private static String combineAddress(String address, String detailAddress) {
		String base = address == null ? "" : address.trim();
		String detail = (detailAddress == null || detailAddress.isBlank()) ? "" : detailAddress.trim();
		return (base + " " + detail).trim();
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record StaySummaryDTO(Long stayId, String imageUrl, String title, String address) {
		public static StaySummaryDTO ofBase(Stay stay, String publicBaseUrl) {
			String imageUrl = publicBaseUrl + "/" + stay.getImages().getFirst().getS3Key();
			return new StaySummaryDTO(stay.getId(), imageUrl, stay.getTitle(), stay.getAddress());
		}

		public static StaySummaryDTO ofFull(Stay stay, String publicBaseUrl) {
			String imageUrl = publicBaseUrl + "/" + stay.getImages().getFirst().getS3Key();
			return new StaySummaryDTO(stay.getId(), imageUrl, stay.getTitle(),
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
}
