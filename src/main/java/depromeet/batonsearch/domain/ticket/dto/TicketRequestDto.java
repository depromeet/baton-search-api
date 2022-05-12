package depromeet.batonsearch.domain.ticket.dto;


import depromeet.batonsearch.domain.ticket.*;
import depromeet.batonsearch.domain.user.User;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

import static depromeet.batonsearch.domain.tag.StaticTag.tagToKey;

public class TicketRequestDto {

    @Getter @Setter
    public static class Search {
        @NotNull(message = "페이지 수를 입력 해 주세요.")
        @Positive(message = "페이지 수는 양수여야 합니다.")
        private Integer page;

        @NotNull(message = "사이즈 수를 입력 해 주세요.")
        @Positive(message = "사이즈 수는 양수여야 합니다.")
        private Integer size;

        @Size(min = 2, max = 50, message = "최소 2자 이상, 최대 50자를 입력 해 주세요")
        private String place;

        private Long tagHash;

        @NotNull(message = "위도를 입력 해 주세요.")
        private Double latitude;

        @NotNull(message = "경도를 입력 해 주세요.")
        private Double longitude;

        @Size(min = 2, max = 50, message = "최소 2자 이상, 최대 50자를 입력 해 주세요")
        private String town;

        @PositiveOrZero(message = "가격은 0보다 커야 합니다.")
        private Long minPrice;

        @PositiveOrZero(message = "가격은 0보다 커야 합니다.")
        private Long maxPrice;

        @PositiveOrZero(message = "남은 일자 혹은 횟수는 0보다 커야 합니다.")
        private Integer minRemainNumber;

        @PositiveOrZero(message = "남은 일자 혹은 횟수는 0보다 커야 합니다.")
        private Integer maxRemainNumber;

        @PositiveOrZero(message = "남은 일자 혹은 횟수는 0보다 커야 합니다.")
        private Integer minRemainDay;

        @PositiveOrZero(message = "남은 일자 혹은 횟수는 0보다 커야 합니다.")
        private Integer maxRemainDay;

        @Min(value = 0, message = "거리는 0km 보다 크거나 같아야 합니다.") @Max(value = 10, message = "거리는 10km 보다 작거나 같아야 합니다.")
        private Double maxDistance;

        private Set<TicketType> types;
        private TicketTradeType tradeType;
        private TicketTransferFee transferFee;
        private TicketState state;
        private TicketSortType sortType;
        private Boolean hasClothes;
        private Boolean hasLocker;
        private Boolean hasShower;
        private Boolean hasGx;
        private Boolean canResell;
        private Boolean canRefund;
        private Boolean isHold;
        private Boolean canNego;
        private Boolean isMembership;

        @Builder
        public Search(Integer page, Integer size, String place, Set<String> hashtag, Double latitude, Double longitude, String town, Long minPrice, Long maxPrice, Integer minRemainNumber, Integer maxRemainNumber, Integer minRemainDay, Integer maxRemainDay, Boolean hasClothes, Boolean hasLocker, Boolean hasShower, Boolean hasGx, Boolean canResell, Boolean canRefund, Boolean isHold, Boolean canNego, Boolean isMembership, Double maxDistance, Set<TicketType> ticketTypes, TicketState ticketState, TicketTradeType ticketTradeType, TicketTransferFee ticketTransferFee, TicketSortType sortType) {
            this.tagHash = 0L;

            if (hashtag != null)
                for (String key: hashtag) {
                    Integer i = tagToKey.get(key);
                    if (i != null)
                        this.tagHash += (1L << (i - 1));
                }

            this.page = page;
            this.size = size;
            this.place = place;
            this.latitude = latitude;
            this.longitude = longitude;
            this.town = town;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.minRemainNumber = minRemainNumber;
            this.maxRemainNumber = maxRemainNumber;
            this.minRemainDay = minRemainDay;
            this.maxRemainDay = maxRemainDay;
            this.hasClothes = hasClothes;
            this.hasLocker = hasLocker;
            this.hasShower = hasShower;
            this.sortType = sortType;
            this.hasGx = hasGx;
            this.canResell = canResell;
            this.canRefund = canRefund;
            this.isHold = isHold;
            this.canNego = canNego;
            this.isMembership = isMembership;
            this.maxDistance = maxDistance;
            this.tradeType = ticketTradeType;
            this.types = ticketTypes;
            this.transferFee = ticketTransferFee;
            this.state = ticketState;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Info {
        // @NotNull: 유저 인증이 붙으면 수정
        User seller;

        @NotBlank
        String location;

        @NotBlank
        String address;

        @NotNull @Min(value = 0)
        Integer price;

        @NotNull
        TicketType type;

        @NotNull
        Boolean canNego;

        @NotNull
        TicketTradeType tradeType;

        @NotNull
        Boolean hasShower;

        @NotNull
        Boolean hasLocker;

        @NotNull
        Boolean hasClothes;

        @NotNull
        Boolean hasGx;

        @NotNull
        Boolean canResell;

        @NotNull
        Boolean canRefund;

        @NotEmpty
        String description;

        @NotNull
        TicketTransferFee transferFee;

        @NotNull
        Double latitude;

        @NotNull
        Double longitude;

        @NotNull
        Boolean isMembership;

        @NotNull
        Boolean isHolding;

        @NotNull @Min(value = 0)
        Integer remainingNumber;

        public Ticket toEntity() {
            return Ticket.builder()
                    .seller(this.seller)
                    .location(this.location)
                    .address(this.address)
                    .createdAt(LocalDateTime.now())
                    .price(this.price)
                    .state(TicketState.SALE)
                    .type(this.type)
                    .canNego(this.canNego)
                    .tradeType(this.tradeType)
                    .hasShower(this.hasShower)
                    .hasLocker(this.hasLocker)
                    .hasClothes(this.hasClothes)
                    .hasGx(this.hasGx)
                    .canResell(this.canResell)
                    .canRefund(this.canRefund)
                    .description(this.description)
                    .transferFee(this.transferFee)
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .isMembership(this.isMembership)
                    .isHolding(this.isHolding)
                    .remainingNumber(this.remainingNumber)
                    .build();
        }
    }
}
