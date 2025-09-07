package com.sido.backend.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sido.backend.reservation.dto.ReservationListFilter;
import com.sido.backend.reservation.entity.QReservation;
import com.sido.backend.reservation.entity.Reservation;
import com.sido.backend.reservation.entity.ResrvStatus;
import com.sido.backend.reservation.entity.VisitStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReservationQDslRepositoryImpl implements ReservationQDslRepository {
	private final JPAQueryFactory qf;

	@Override
	public Slice<Reservation> findList(Long memberId, ReservationListFilter filter, Pageable pageable) {
		QReservation qr = QReservation.reservation;

		BooleanBuilder where = new BooleanBuilder().and(qr.member.id.eq(memberId));

		return findCommon(where, filter, pageable);
	}

	@Override
	public Slice<Reservation> findAdminList(Long memberId, ReservationListFilter filter, Long stayId,
		Pageable pageable) {
		QReservation qr = QReservation.reservation;

		BooleanBuilder where = new BooleanBuilder().and(qr.stay.host.id.eq(memberId));

		// stayId가 있으면 필터링 추가
		if (stayId != null) {
			where.and(qr.stay.id.eq(stayId));
		}

		return findCommon(where, filter, pageable);
	}

	@Override
	@Transactional
	public long bulkUpdateVisitStatus(LocalDate today) {
		QReservation qr = QReservation.reservation;

		long updated = 0;

		updated += qf.update(qr)
			.set(qr.visitStatus, VisitStatus.IN_PROGRESS)
			.where(qr.resrvStatus.eq(ResrvStatus.RESERVED)
				.and(qr.startDate.loe(today))
				.and(qr.endDate.goe(today))
			).execute();

		updated += qf.update(qr)
			.set(qr.visitStatus, VisitStatus.UPCOMING)
			.where(qr.resrvStatus.eq(ResrvStatus.RESERVED)
				.and(qr.startDate.gt(today))
			).execute();

		updated += qf.update(qr)
			.set(qr.visitStatus, VisitStatus.COMPLETED)
			.where(qr.resrvStatus.eq(ResrvStatus.RESERVED)
				.and(qr.endDate.lt(today))
			).execute();

		updated += qf.update(qr)
			.setNull(qr.visitStatus)
			.where(qr.resrvStatus.in(ResrvStatus.PENDING, ResrvStatus.CANCELLED)
			).execute();

		return updated;
	}

	private Slice<Reservation> findCommon(BooleanBuilder where, ReservationListFilter filter, Pageable pageable) {
		QReservation qr = QReservation.reservation;

		// 필터: 전체 / 예약 됨(방문 전, 방문 중) / 방문 완료 / 취소 됨
		switch (filter) {
			case ReservationListFilter.RESERVED -> where.and(qr.resrvStatus.eq(ResrvStatus.RESERVED)
				.and(qr.visitStatus.in(VisitStatus.UPCOMING, VisitStatus.IN_PROGRESS)));
			case ReservationListFilter.COMPLETED -> where.and(qr.resrvStatus.eq(ResrvStatus.RESERVED)
				.and(qr.visitStatus.eq(VisitStatus.COMPLETED)));
			case ReservationListFilter.CANCELLED -> where.and(qr.resrvStatus.eq(ResrvStatus.CANCELLED));
			case ReservationListFilter.ALL -> where.and(qr.resrvStatus.in(ResrvStatus.RESERVED, ResrvStatus.CANCELLED));
		}

		// 정렬 (1) 그룹 우선순위
		// 전체 탭 기준- 방문 중 -> 방문 전 -> 방문 완료 -> 예약 취소
		NumberExpression<Integer> priority = new CaseBuilder()
			.when(qr.visitStatus.eq(VisitStatus.IN_PROGRESS)).then(0)
			.when(qr.visitStatus.eq(VisitStatus.UPCOMING)).then(1)
			.when(qr.visitStatus.eq(VisitStatus.COMPLETED)).then(2)
			.when(qr.resrvStatus.eq(ResrvStatus.CANCELLED)).then(3)
			.otherwise(4);

		// 정렬 (2) 주 정렬키
		// 방문 중: endDate ASC, 나머지: startDate ASC
		ComparableExpressionBase<?> primaryKey = new CaseBuilder()
			.when(qr.visitStatus.eq(VisitStatus.IN_PROGRESS)).then(qr.endDate)
			.otherwise(qr.startDate);

		// 정렬 (3) 보조 정렬키
		// 방문 중: startDate ASC, 나머지: endDate ASC
		ComparableExpressionBase<?> secondKey = new CaseBuilder()
			.when(qr.visitStatus.eq(VisitStatus.IN_PROGRESS)).then(qr.startDate)
			.otherwise(qr.endDate);

		// 필터별 시간 순 정렬 적용
		JPAQuery<Reservation> query = qf.selectFrom(qr)
			.where(where)
			.orderBy(
				priority.asc(),
				primaryKey.asc().nullsLast(),
				secondKey.asc().nullsLast(),
				qr.id.asc() // 정렬 (4) 그래도 같으면 id ASC
			);

		// Slice 방식
		int size = pageable.getPageSize();
		List<Reservation> rows = query
			.offset(pageable.getOffset())
			.limit(size + 1)
			.fetch();

		boolean hasNext = rows.size() > size;
		if (hasNext)
			rows.remove(size);

		return new SliceImpl<>(rows, pageable, hasNext);
	}
}
