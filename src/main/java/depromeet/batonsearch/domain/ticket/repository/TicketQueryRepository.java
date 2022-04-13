package depromeet.batonsearch.domain.ticket.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import depromeet.batonsearch.domain.Tag.Tag;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.max;
import static com.querydsl.core.types.dsl.Expressions.list;
import static com.querydsl.core.types.dsl.Expressions.set;
import static depromeet.batonsearch.domain.user.QUser.user;
import static depromeet.batonsearch.domain.Tag.QTag.tag;
import static depromeet.batonsearch.domain.ticket.QTicket.ticket;

@RequiredArgsConstructor
@Repository
public class TicketQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    public Page<TicketResponseDto.Simple> searchAll(TicketRequestDto.Search search, Pageable pageable) {
        List<TicketResponseDto.Simple> results = queryFactory.selectFrom(ticket)
                .innerJoin(ticket.tags, tag)
                .fetchJoin()
                .innerJoin(ticket.seller, user)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(
                    hasTag(search.getHashtag()),
                    likePlace(search.getPlace()),
                    priceGoe(search.getMinPrice()),
                    priceLoe(search.getMaxPrice())
                )
                .distinct()
                .fetch()
                .stream()
                .map(TicketResponseDto.Simple::of)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanExpression hasTag(Set<String> tags) {
        if (tags == null)
            return null;

        return ticket.tags.any().subject.in(tags);
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
}
