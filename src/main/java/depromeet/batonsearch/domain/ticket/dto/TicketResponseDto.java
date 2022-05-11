package depromeet.batonsearch.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import depromeet.batonsearch.domain.ticket.*;
import depromeet.batonsearch.domain.ticketimage.TicketImage;
import depromeet.batonsearch.domain.user.User;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        private Double latitude;
        private Double longitude;
        private Double distance;

        @Builder
        @QueryProjection
        public Simple(Integer id, String location, String address, Integer price, TicketState state, LocalDateTime createAt, Boolean isMembership, Integer remainingNumber, Long tagHash, Point point, Double distance) {
            this.id = id;
            this.location = location;
            this.address = address;
            this.price = price;
            this.createAt = createAt;
            this.state = state;
            this.isMembership = isMembership;
            this.remainingNumber = remainingNumber;
            this.latitude = point.getY();
            this.longitude = point.getX();
            this.distance = distance;
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
                .point(ticket.getPoint())
                .tagHash(ticket.getTagHash())
                .createAt(ticket.getCreatedAt())
                .isMembership(ticket.getIsMembership())
                .remainingNumber(ticket.getRemainingNumber())
                .build();
        }
    }

    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class Info {
        private Integer id;
        private User seller;
        private String location;
        private String address;
        private Integer price;
        private LocalDateTime createAt;
        private TicketState state;
        private TicketType type;
        private TicketTradeType tradeType;
        private TicketTransferFee transferFee;
        private Boolean canNego;
        private Boolean hasShower;
        private Boolean hasLocker;
        private Boolean hasClothes;
        private Boolean hasGx;
        private Boolean canResell;
        private Boolean canRefund;
        private String description;
        private Set<String> tags;
        private Set<String> images;
        private Boolean isMembership;
        private Boolean isHolding;
        private Integer remainingNumber;
        private Double latitude;
        private Double longitude;
        private Double distance;

        public static Info of(Ticket ticket) {
            Set<String> tags = new HashSet<>();
            Long tagHash = ticket.getTagHash();

            for (int i = 1; i <= keyToTag.size(); i++) {
                if (tagHash == 0)
                    break;
                else if (tagHash % 2 == 1)
                    tags.add(keyToTag.get(i));
                tagHash /= 2;
            }

            return Info.builder()
                    .id(ticket.getId())
                    .seller(ticket.getSeller())
                    .location(ticket.getLocation())
                    .address(ticket.getAddress())
                    .price(ticket.getPrice())
                    .createAt(ticket.getCreatedAt())
                    .state(ticket.getState())
                    .type(ticket.getType())
                    .tradeType(ticket.getTradeType())
                    .transferFee(ticket.getTransferFee())
                    .canNego(ticket.getCanNego())
                    .hasShower(ticket.getHasShower())
                    .hasLocker(ticket.getHasLocker())
                    .hasClothes(ticket.getHasClothes())
                    .hasGx(ticket.getHasGx())
                    .canResell(ticket.getCanResell())
                    .canRefund(ticket.getCanRefund())
                    .description(ticket.getDescription())
                    .images(
                            ticket.getImages().stream().map(TicketImage::getUrl)
                                    .collect(Collectors.toSet())
                    )
                    .tags(tags)
                    .isMembership(ticket.getIsMembership())
                    .isHolding(ticket.getIsHolding())
                    .remainingNumber(ticket.getRemainingNumber())
                    .latitude(ticket.getPoint().getY())
                    .longitude(ticket.getPoint().getX())
                    .build();
        }

    }
}
