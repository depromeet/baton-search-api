package depromeet.batonsearch.domain.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import depromeet.batonsearch.domain.tag.TagEnum;
import depromeet.batonsearch.domain.ticket.*;
import depromeet.batonsearch.domain.ticketimage.dto.TicketImageResponseDto;
import depromeet.batonsearch.domain.user.dto.UserResponseDto;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class TicketResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Inquiry {
        private Integer id;
        private String location;
        private TicketType type;
        private Integer price;
        private Boolean canNego;
        private TicketTradeType tradeType;

        public static Inquiry of(Ticket ticket) {
            return Inquiry.builder()
                    .id(ticket.getId())
                    .location(ticket.getLocation())
                    .type(ticket.getType())
                    .price(ticket.getPrice())
                    .canNego(ticket.getCanNego())
                    .tradeType(ticket.getTradeType())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Simple {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate expiryDate;

        private Integer id;
        private String location;
        private String address;
        private Integer price;
        private String mainImage;
        private Set<String> tags;
        private Boolean isMembership;
        private Integer remainingNumber;
        private Long remainingDay;
        private TicketState state;
        private Double latitude;
        private Double longitude;
        private Double distance;
        private Integer bookmarkCount;
        private Integer viewCount;
        private TicketType type;

        @Builder
        @QueryProjection
        public Simple(Integer id, String location, String address, Integer price, TicketState state, LocalDateTime createAt, Boolean isMembership, Integer remainingNumber, Long tagHash, Point point, Double distance, String mainImage, LocalDate expiryDate, Integer viewCount, Integer bookmarkCount, TicketType type) {
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
            this.mainImage = mainImage;
            this.expiryDate = expiryDate;
            this.bookmarkCount = bookmarkCount;
            this.viewCount = viewCount;
            this.tags = new HashSet<>();
            this.type = type;

            for (int i = 1; i <= TagEnum.values().length; i++) {
                if (tagHash == 0)
                    break;
                else if (tagHash % 2 == 1)
                    tags.add(TagEnum.values()[i - 1].getContent());
                tagHash /= 2;
            }

            if (this.expiryDate != null)
                this.remainingDay = ChronoUnit.DAYS.between(LocalDate.now(), this.expiryDate);
        }

        public static Simple of(Ticket ticket) {
            return Simple.builder()
                .id(ticket.getId())
                .createAt(ticket.getCreatedAt())
                .expiryDate(ticket.getExpiryDate())
                .location(ticket.getLocation())
                .address(ticket.getAddress())
                .price(ticket.getPrice())
                .state(ticket.getState())
                .point(ticket.getPoint())
                .tagHash(ticket.getTagHash())
                .createAt(ticket.getCreatedAt())
                .isMembership(ticket.getIsMembership())
                .remainingNumber(ticket.getRemainingNumber())
                .mainImage(ticket.getMainImage())
                .bookmarkCount(ticket.getBookmarkCount())
                .viewCount(ticket.getViewCount())
                .type(ticket.getType())
                .build();
        }
    }

    @Getter @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class Info {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate expiryDate;

        private Integer id;
        private UserResponseDto seller;
        private String location;
        private String address;
        private Integer price;
        private TicketState state;
        private TicketType type;
        private TicketTradeType tradeType;
        private TicketTransferFee transferFee;
        private Integer bookmarkId;
        private Boolean canNego;
        private Boolean hasShower;
        private Boolean hasLocker;
        private Boolean hasClothes;
        private Boolean hasGx;
        private Boolean canResell;
        private Boolean canRefund;
        private String description;
        private Set<String> tags;
        private Set<TicketImageResponseDto> images;
        private Boolean isMembership;
        private Boolean isHolding;
        private Integer remainingNumber;
        private Long remainingDay;
        private Double latitude;
        private Double longitude;
        private Double distance;
        private Integer bookmarkCount;
        private Integer viewCount;


        public static Info of(Ticket ticket) {
            Set<String> tags = new HashSet<>();
            Long tagHash = ticket.getTagHash();

            for (int i = 1; i <= TagEnum.values().length; i++) {
                if (tagHash == 0)
                    break;
                else if (tagHash % 2 == 1)
                    tags.add(TagEnum.values()[i - 1].getContent());
                tagHash /= 2;
            }

            return Info.builder()
                    .id(ticket.getId())
                    .seller(UserResponseDto.of(ticket.getSeller()))
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
                    .images(ticket.getImages().stream().map(TicketImageResponseDto::of).collect(Collectors.toSet()))
                    .tags(tags)
                    .isMembership(ticket.getIsMembership())
                    .isHolding(ticket.getIsHolding())
                    .remainingNumber(ticket.getRemainingNumber())
                    .remainingDay(ticket.getExpiryDate() != null ? ChronoUnit.DAYS.between(LocalDate.now(), ticket.getExpiryDate()) : null)
                    .latitude(ticket.getPoint().getY())
                    .longitude(ticket.getPoint().getX())
                    .expiryDate(ticket.getExpiryDate())
                    .bookmarkCount(ticket.getBookmarkCount())
                    .viewCount(ticket.getViewCount())
                    .build();
        }

        public static double distance(double lat1, double lon1, double lat2, double lon2) {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));

            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            return dist * 60 * 1.1515 * 1609.344;
        }
    }
}
