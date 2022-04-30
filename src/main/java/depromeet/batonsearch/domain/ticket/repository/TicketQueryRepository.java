package depromeet.batonsearch.domain.ticket.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import depromeet.batonsearch.domain.ticket.dto.QTicketResponseDto_Simple;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.global.util.Direction;
import depromeet.batonsearch.global.util.GeometryUtil;
import depromeet.batonsearch.global.util.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static depromeet.batonsearch.domain.ticket.QTicket.ticket;


@RequiredArgsConstructor
@Repository
public class TicketQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Page<TicketResponseDto.Simple> searchAll(TicketRequestDto.Search search, Pageable pageable) {

        List<TicketResponseDto.Simple> results = queryFactory.select(new QTicketResponseDto_Simple(
                        ticket.id, ticket.location, ticket.address, ticket.price, ticket.state, ticket.createdAt, ticket.isMembership, ticket.remainingNumber, ticket.tagHash, ticket.point
                ))
                .from(ticket)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(
                    distanceLoe(search.getLatitude(), search.getLongitude(), search.getMaxDistance()),
                    isMembership(search.getIsMembership()),
                    likePlace(search.getPlace()),
                    priceGoe(search.getMinPrice()),
                    priceLoe(search.getMaxPrice()),
                    remainNumberGoe(search.getMinRemainNumber()),
                    remainNumberLoe(search.getMaxRemainNumber()),
                    hasTag(search.getTagHash())
                )
                .fetch();
        return new PageImpl<>(results, pageable, results.size());
    }


    private BooleanExpression hasTag(Long tagHash) {
        if (tagHash == null) return null;
        else if (tagHash == 0) return null;
        return Expressions.numberTemplate(Long.class, "function('bitand', {0}, {1})", tagHash, ticket.tagHash).eq(tagHash);
    }

    private BooleanExpression likePlace(String place) {
        return StringUtils.hasText(place) ? ticket.location.like(place) : null;
    }

    private BooleanExpression priceGoe(Long minPrice) {
        return minPrice != null ? ticket.price.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(Long maxPrice) {
        return maxPrice != null ? ticket.price.loe(maxPrice) : null;
    }

    private BooleanExpression isMembership(Boolean bool) {
        return bool != null ? ticket.isMembership.eq(bool) : null;
    }

    private BooleanExpression remainNumberGoe(Integer minRemainNumber) {
        return minRemainNumber != null ? ticket.remainingNumber.goe(minRemainNumber) : null;
    }

    private BooleanExpression remainNumberLoe(Integer minRemainNumber) {
        return minRemainNumber != null ? ticket.remainingNumber.loe(minRemainNumber) : null;
    }

    private BooleanExpression distanceLoe(Double latitude, Double longitude, Double distance) {
        if (latitude == null || longitude == null || distance == null)
            return null;

        Location northEast = GeometryUtil.calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());

        double x1 = northEast.getLatitude();
        double y1 = northEast.getLongitude();
        double x2 = southWest.getLatitude();
        double y2 = southWest.getLongitude();

        return Expressions.booleanTemplate("function('geocontains', {0}, {1})", String.format("LINESTRING(%f %f, %f %f)", x1, y1, x2, y2), ticket.point).eq(true);
    }
}
