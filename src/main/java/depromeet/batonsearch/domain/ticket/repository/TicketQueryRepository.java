package depromeet.batonsearch.domain.ticket.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import depromeet.batonsearch.domain.ticket.*;
import depromeet.batonsearch.domain.ticket.dto.QTicketResponseDto_Simple;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.global.util.Direction;
import depromeet.batonsearch.global.util.GeometryUtil;
import depromeet.batonsearch.global.util.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static depromeet.batonsearch.domain.ticket.QTicket.ticket;


@RequiredArgsConstructor
@Repository
public class TicketQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Long countSearch(TicketRequestDto.Search search) {
        return queryFactory.select(Wildcard.count)
                .from(ticket)
                .where(
                        distanceLoe(search.getLatitude(), search.getLongitude(), search.getMaxDistance()),
                        likePlace(search.getPlace()),
                        likeTown(search.getTown()),
                        priceGoe(search.getMinPrice()),
                        priceLoe(search.getMaxPrice()),
                        remainSearch(search.getMinRemainNumber(), search.getMaxRemainNumber(), search.getMinRemainMonth(), search.getMaxRemainMonth()),
                        ticketStateCheck(search.getState()),
                        ticketTypeCheck(search.getTypes()),
                        ticketTradeTypeCheck(search.getTradeType()),
                        ticketTransferFeeCheck(search.getTransferFee()),
                        hasTag(search.getTagHash()),
                        hasGxCheck(search.getHasGx()),
                        hasClothesCheck(search.getHasClothes()),
                        hasShowerCheck(search.getHasShower()),
                        hasLockerCheck(search.getHasLocker()),
                        canResellCheck(search.getCanResell()),
                        canRefundCheck(search.getCanRefund()),
                        canNegoCheck(search.getCanNego()),
                        isHoldCheck(search.getIsHold())
                ).fetch().get(0);
    }

    @Transactional(readOnly = true)
    public Page<TicketResponseDto.Simple> stringSearch(TicketRequestDto.StringSearch search, Pageable pageable) {
        List<TicketResponseDto.Simple> results = queryFactory.select(new QTicketResponseDto_Simple(
                ticket.id,
                ticket.location,
                ticket.address,
                ticket.price,
                ticket.state,
                ticket.createdAt,
                ticket.isMembership,
                ticket.remainingNumber,
                ticket.tagHash,
                ticket.point,
                Expressions.numberTemplate(
                        Double.class, "function('calc_distance', {0}, {1})", String.format("point(%f %f)", search.getLatitude(), search.getLongitude()), ticket.point
                ).as("distance"),
                ticket.mainImage,
                ticket.expiryDate,
                ticket.bookmarkCount
            ))
            .from(ticket)
            .where(
                    distanceLoe(search.getLatitude(), search.getLongitude(), search.getMaxDistance()),
                    singleStringQuery(search.getQuery())
            )
            .orderBy(
                    orderSelect(TicketSortType.RECENT)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Ticket> countQuery = queryFactory.selectFrom(ticket)
                .where(
                        distanceLoe(search.getLatitude(), search.getLongitude(), search.getMaxDistance()),
                        singleStringQuery(search.getQuery())
                );

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().size());

    }

    @Transactional(readOnly = true)
    public Page<TicketResponseDto.Simple> searchAll(TicketRequestDto.Search search, Pageable pageable) {
        List<TicketResponseDto.Simple> results = queryFactory.select(new QTicketResponseDto_Simple(
                    ticket.id,
                    ticket.location,
                    ticket.address,
                    ticket.price,
                    ticket.state,
                    ticket.createdAt,
                    ticket.isMembership,
                    ticket.remainingNumber,
                    ticket.tagHash,
                    ticket.point,
                    Expressions.numberTemplate(
                            Double.class, "function('calc_distance', {0}, {1})", String.format("point(%f %f)", search.getLatitude(), search.getLongitude()), ticket.point
                    ).as("distance"),
                    ticket.mainImage,
                    ticket.expiryDate,
                    ticket.bookmarkCount
                ))
                .from(ticket)
                .where(
                    distanceLoe(search.getLatitude(), search.getLongitude(), search.getMaxDistance()),
                    likePlace(search.getPlace()),
                    likeTown(search.getTown()),
                    priceGoe(search.getMinPrice()),
                    priceLoe(search.getMaxPrice()),
                    remainSearch(search.getMinRemainNumber(), search.getMaxRemainNumber(), search.getMinRemainMonth(), search.getMaxRemainMonth()),
                    ticketStateCheck(search.getState()),
                    ticketTypeCheck(search.getTypes()),
                    ticketTradeTypeCheck(search.getTradeType()),
                    ticketTransferFeeCheck(search.getTransferFee()),
                    hasTag(search.getTagHash()),
                    hasGxCheck(search.getHasGx()),
                    hasClothesCheck(search.getHasClothes()),
                    hasShowerCheck(search.getHasShower()),
                    hasLockerCheck(search.getHasLocker()),
                    canResellCheck(search.getCanResell()),
                    canRefundCheck(search.getCanRefund()),
                    canNegoCheck(search.getCanNego()),
                    isHoldCheck(search.getIsHold())
                )
                .orderBy(
                        orderSelect(search.getSortType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(Wildcard.count)
                .from(ticket)
                .where(
                    distanceLoe(search.getLatitude(), search.getLongitude(), search.getMaxDistance()),
                    likePlace(search.getPlace()),
                    likeTown(search.getTown()),
                    priceGoe(search.getMinPrice()),
                    priceLoe(search.getMaxPrice()),
                    remainSearch(search.getMinRemainNumber(), search.getMaxRemainNumber(), search.getMinRemainMonth(), search.getMaxRemainMonth()),
                    ticketStateCheck(search.getState()),
                    ticketTypeCheck(search.getTypes()),
                    ticketTradeTypeCheck(search.getTradeType()),
                    ticketTransferFeeCheck(search.getTransferFee()),
                    hasTag(search.getTagHash()),
                    hasGxCheck(search.getHasGx()),
                    hasClothesCheck(search.getHasClothes()),
                    hasShowerCheck(search.getHasShower()),
                    hasLockerCheck(search.getHasLocker()),
                    canResellCheck(search.getCanResell()),
                    canRefundCheck(search.getCanRefund()),
                    canNegoCheck(search.getCanNego()),
                    isHoldCheck(search.getIsHold())
                );

        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetch().get(0));
    }


    private BooleanExpression hasTag(Long tagHash) {
        if (tagHash == null) return null;
        else if (tagHash == 0) return null;
        return Expressions.numberTemplate(Long.class, "function('bitand', {0}, {1})", tagHash, ticket.tagHash).eq(tagHash);
    }

    private BooleanExpression likePlace(String place) {
        return StringUtils.hasText(place) ? ticket.location.like('%' + place + '%') : null;
    }

    private BooleanExpression likeTown(String town) {
        return StringUtils.hasText(town) ? ticket.address.like('%' + town + '%') : null;
    }

    private BooleanExpression priceGoe(Long minPrice) {
        return minPrice != null ? ticket.price.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(Long maxPrice) {
        return maxPrice != null ? ticket.price.loe(maxPrice) : null;
    }

    private BooleanExpression remainNumberSearch(Integer minRemainNumber, Integer maxRemainNumber) {
        if (minRemainNumber == null || maxRemainNumber == null)
            return null;

        return ticket.isMembership.eq(false).and(ticket.remainingNumber.between(minRemainNumber, maxRemainNumber));
    }

    private BooleanExpression remainMonthSearch(Integer minRemainMonth, Integer maxRemainMonth) {
        if (minRemainMonth == null || maxRemainMonth == null)
            return null;

        LocalDate minDate = LocalDate.now().plusMonths(minRemainMonth);
        LocalDate maxDate = LocalDate.now().plusMonths(maxRemainMonth);

        return ticket.isMembership.eq(true).and(ticket.expiryDate.between(minDate, maxDate));
    }

    private BooleanExpression remainSearch(Integer minRemainNumber, Integer maxRemainNumber, Integer minRemainMonth, Integer maxRemainMonth) {
        BooleanExpression remainNumberExpression = remainNumberSearch(minRemainNumber, maxRemainNumber);
        BooleanExpression expiryDayExpression = remainMonthSearch(minRemainMonth, maxRemainMonth);

        if (remainNumberExpression == null) {
            return expiryDayExpression;
        } else if (expiryDayExpression == null) {
            return remainNumberExpression;
        } else {
            return expiryDayExpression.or(remainNumberExpression);
        }
    }

    private BooleanExpression singleStringQuery(String query) {
        if (query == null)
            return null;
        else
            return likePlace(query).or(likeTown(query));
    }

    private BooleanExpression distanceLoe(Double latitude, Double longitude, Double distance) {
        if (latitude == null || longitude == null || distance == null)
            return null;

        Location northEast = GeometryUtil.calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
        Location southWest = GeometryUtil.calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());

        double x1 = northEast.getLongitude();
        double y1 = northEast.getLatitude();
        double x2 = southWest.getLongitude();
        double y2 = southWest.getLatitude();

        return Expressions.booleanTemplate("function('geocontains', {0}, {1})", String.format("LINESTRING(%f %f, %f %f)", y1, x1, y2, x2), ticket.point).eq(true);
    }

    private BooleanExpression ticketTypeCheck(Set<TicketType> types) {
        return types != null ? ticket.type.in(types) : null;
    }

    private BooleanExpression ticketStateCheck(TicketState state) {
        return state != null ? ticket.state.eq(state) : null;
    }

    private BooleanExpression ticketTradeTypeCheck(TicketTradeType tradeType) {
        return tradeType != null ? ticket.tradeType.eq(tradeType) : null;
    }

    private BooleanExpression ticketTransferFeeCheck(TicketTransferFee transferFee) {
        return transferFee != null ? ticket.transferFee.eq(transferFee) : null;
    }

    private BooleanExpression hasShowerCheck(Boolean hasShower) {
        return hasShower != null ? ticket.hasShower.eq(hasShower) : null;
    }

    private BooleanExpression hasLockerCheck(Boolean hasLocker) {
        return hasLocker != null ? ticket.hasLocker.eq(hasLocker) : null;
    }

    private BooleanExpression hasClothesCheck(Boolean hasClothes) {
        return hasClothes != null ? ticket.hasClothes.eq(hasClothes) : null;
    }

    private BooleanExpression hasGxCheck(Boolean hasGx) {
        return hasGx != null ? ticket.hasGx.eq(hasGx) : null;
    }

    private BooleanExpression canNegoCheck(Boolean canNego) {
        return canNego != null ? ticket.canNego.eq(canNego) : null;
    }

    private BooleanExpression canResellCheck(Boolean canResell) {
        return canResell != null ? ticket.canResell.eq(canResell) : null;
    }

    private BooleanExpression canRefundCheck(Boolean canRefund) {
        return canRefund != null ? ticket.canRefund.eq(canRefund) : null;
    }

    private BooleanExpression isHoldCheck(Boolean isHold) {
        return isHold != null ? ticket.isHolding.eq(isHold) : null;
    }

    private OrderSpecifier<? extends Comparable> orderSelect(TicketSortType sortType) {
        if (sortType == null)
            return ticket.id.desc();

        switch (sortType) {
            case REMAIN_DAY:
                return ticket.remainingNumber.desc();
            case LOWER_PRICE:
                return ticket.price.asc();
            case HIGHER_PRICE:
                return ticket.price.desc();
            case DISTANCE:
                return Expressions.stringTemplate("distance").asc();
            default:
                return ticket.id.desc();
        }
    }
}
