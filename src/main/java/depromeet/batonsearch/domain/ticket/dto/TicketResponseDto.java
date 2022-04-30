package depromeet.batonsearch.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.TicketState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static depromeet.batonsearch.domain.tag.StaticTag.keyToTag;

public class TicketResponseDto {
    @ApiModel(value = "양도권 정보 축약형 모델")
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Simple {
        private Integer id;
        private String location;
        private String address;
        private Integer price;
        private LocalDateTime createAt;
        private Set<String> tags;
        private Boolean isMembership;
        private Integer remainingNumber;
        private TicketState state;
        private Point point;

        @Builder
        @QueryProjection
        public Simple(Integer id, String location, String address, Integer price, TicketState state, LocalDateTime createAt, Boolean isMembership, Integer remainingNumber, Long tagHash, Point point) {
            this.id = id;
            this.location = location;
            this.address = address;
            this.price = price;
            this.createAt = createAt;
            this.state = state;
            this.isMembership = isMembership;
            this.remainingNumber = remainingNumber;
            this.point = point;
            this.tags = new HashSet<>();

            for (int i = 1; i <= keyToTag.size(); i++) {
                if (tagHash == 0)
                    break;
                else if (tagHash % 2 == 1)
                    tags.add(keyToTag.get(i));
                tagHash /= 2;
            }
        }

        public static Simple of(Ticket ticket) {
            return Simple.builder()
                .id(ticket.getId())
                .location(ticket.getLocation())
                .price(ticket.getPrice())
                .state(ticket.getState())
                .tagHash(ticket.getTagHash())
                .createAt(ticket.getCreatedAt())
                .isMembership(ticket.getIsMembership())
                .remainingNumber(ticket.getRemainingNumber())
                .build();
        }
    }
}
