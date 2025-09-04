package com.sido.backend.stay.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sido.backend.stay.dto.OpenAndReservedDatesDTO;
import com.sido.backend.common.dto.PageResponseDTO;
import com.sido.backend.stay.dto.AvailDatesDTO;
import com.sido.backend.stay.dto.StayCreateDTO;
import com.sido.backend.stay.dto.StayResponseDTO;
import com.sido.backend.stay.dto.StayResponseDetailDTO;
import com.sido.backend.stay.dto.StayResrvStatus;
import com.sido.backend.stay.dto.StayUpdateDTO;
import com.sido.backend.stay.entity.Stay;
import com.sido.backend.stay.service.StayService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/stays")
@Tag(name = "사랑방-Admin")
@RequiredArgsConstructor
public class StayAdminController {
	private final StayService stayService;

	@Operation(summary = "우리 동네 사랑방 전체 조회")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping
	public ResponseEntity<PageResponseDTO<StayResponseDTO, Stay>> getAdminStays(
		@AuthenticationPrincipal(expression = "memberId") Long memberId,
		@RequestParam(required = false) String roomStatus,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "15") int listSize) {

		StayResrvStatus statusFilter = mapRoomStatus(roomStatus);

		PageRequest pageable = PageRequest.of(page - 1, listSize);
		PageResponseDTO<StayResponseDTO, Stay> result =
			stayService.getStaysByHost(memberId, pageable, statusFilter);

		return ResponseEntity.ok(result);
	}

	@Operation(description = "등록")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<StayResponseDetailDTO> addStay(
		@AuthenticationPrincipal(expression = "memberId") Long memberId,
		@Valid @RequestBody StayCreateDTO stayCreateDTO) {
		return ResponseEntity.ok(stayService.addStay(memberId, stayCreateDTO));
	}

	@Operation(description = "수정")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PatchMapping({"/{stayId}"})
	public ResponseEntity<StayUpdateDTO> editStay(@PathVariable long stayId,
		@AuthenticationPrincipal(expression = "memberId") Long memberId, @Valid @RequestBody StayUpdateDTO stayDTO) {
		return ResponseEntity.ok(stayService.editStay(stayId, memberId, stayDTO));
	}

	@Operation(description = "삭제")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{stayId}")
	public ResponseEntity<Void> deleteStay(@PathVariable Long stayId) {
		stayService.deleteStay(stayId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "월별 오픈한 날짜 & 예약된 날짜 조회", description = "시골 관리자: 사랑방 목록 관리- 예약 가능 날짜 변경하기")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{stayId}/open-dates")
	public ResponseEntity<OpenAndReservedDatesDTO> getOpenAndReservedDatesByMonth(@PathVariable Long stayId,
		@Schema(example = "2025-09") @DateTimeFormat(pattern = "yyyy-MM") @RequestParam(required = false)
		YearMonth month) {
		OpenAndReservedDatesDTO openAndReservedDates = stayService.getOpenAndReservedDatesByMonth(stayId, month);
		return ResponseEntity.ok(openAndReservedDates);
	}

	@Operation(summary = "예약 가능 날짜(오픈 날짜) 추가 및 변경")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{stayId}/open-dates")
	public ResponseEntity<OpenAndReservedDatesDTO> updateOpenDates(@PathVariable @Schema(example = "13") Long stayId,
		@RequestParam("dates") List<LocalDate> dates) {
		OpenAndReservedDatesDTO openAndReservedDates = stayService.updateOpenDates(stayId, dates);
		return ResponseEntity.ok(openAndReservedDates);
	}

	private StayResrvStatus mapRoomStatus(String roomStatus) {
		if (roomStatus == null || roomStatus.equals("전체")) {
			return null; // 전체 조회
		}
		return switch (roomStatus) {
			case "예약 가능" -> StayResrvStatus.AVAILABLE;
			case "예약 마감" -> StayResrvStatus.SOLD_OUT;
			case "예약 닫힘" -> StayResrvStatus.CLOSED;
			default -> throw new IllegalArgumentException("Unknown status: " + roomStatus);
		};
	}

}
