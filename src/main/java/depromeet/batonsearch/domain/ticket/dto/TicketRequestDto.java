package depromeet.batonsearch.domain.ticket.dto;


import depromeet.batonsearch.domain.tag.TagEnum;
import depromeet.batonsearch.domain.ticket.*;
import depromeet.batonsearch.domain.user.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
public class TicketRequestDto {

    @Getter @Setter
    @Builder
    @AllArgsConstructor
    public static class Put {
        private TicketState ticketState;
    }

    @Getter
    public static class Search {
        @NotNull(message = "페이지 수를 입력 해 주세요.")
        @PositiveOrZero(message = "페이지 수는 0 혹은 양수여야 합니다.")
        private final Integer page;

        @NotNull(message = "사이즈 수를 입력 해 주세요.")
        @Positive(message = "사이즈 수는 양수여야 합니다.")
        private final Integer size;

        @Size(min = 2, max = 50, message = "최소 2자 이상, 최대 50자를 입력 해 주세요")
        private final String place;

        private Long tagHash;

        @NotNull(message = "위도를 입력 해 주세요.")
        private final Double latitude;

        @NotNull(message = "경도를 입력 해 주세요.")
        private final Double longitude;

        @Size(min = 2, max = 50, message = "최소 2자 이상, 최대 50자를 입력 해 주세요")
        private final String town;

        @PositiveOrZero(message = "가격은 0보다 커야 합니다.")
        private final Long minPrice;

        @PositiveOrZero(message = "가격은 0보다 커야 합니다.")
        private final Long maxPrice;

        @PositiveOrZero(message = "남은 일자 혹은 횟수는 0보다 커야 합니다.")
        private final Integer minRemainNumber;

        @PositiveOrZero(message = "남은 일자 혹은 횟수는 0보다 커야 합니다.")
        private final Integer maxRemainNumber;

        @Min(value = 0, message = "남은 개월 수는 0보단 같거나 커야 합니다.")
        @Max(value = 12, message = "남은 개월 수는 12보단 같거나 작아야 합니다.")
        private final Integer minRemainMonth;

        @Min(value = 0, message = "남은 개월 수는 0보단 같거나 커야 합니다.")
        @Max(value = 12, message = "남은 개월 수는 12보단 같거나 작아야 합니다.")
        private final Integer maxRemainMonth;

        @Min(value = 0, message = "거리는 0km 보다 크거나 같아야 합니다.") @Max(value = 20000, message = "거리는 20km (20000m) 보다 작거나 같아야 합니다.")
        @NotNull(message = "거리를 입력 하여야 합니다.")
        private final Double maxDistance;

        private final Set<TicketType> types;
        private final TicketTradeType tradeType;
        private final TicketTransferFee transferFee;
        private final TicketState state;
        private final TicketSortType sortType;
        private final Boolean hasClothes;
        private final Boolean hasLocker;
        private final Boolean hasShower;
        private final Boolean hasGx;
        private final Boolean canResell;
        private final Boolean canRefund;
        private final Boolean isHold;
        private final Boolean canNego;
        private final Boolean isMembership;

        @Builder
        public Search(Integer page, Integer size, String place, Set<String> hashtag, Double latitude, Double longitude, String town, Long minPrice, Long maxPrice, Integer minRemainNumber, Integer maxRemainNumber, Integer minRemainMonth, Integer maxRemainMonth, Boolean hasClothes, Boolean hasLocker, Boolean hasShower, Boolean hasGx, Boolean canResell, Boolean canRefund, Boolean isHold, Boolean canNego, Boolean isMembership, Double maxDistance, Set<TicketType> ticketTypes, TicketState ticketState, TicketTradeType ticketTradeType, TicketTransferFee ticketTransferFee, TicketSortType sortType) {
            this.tagHash = 0L;

            try {
                if (hashtag != null)
                    for (String key: hashtag) {
                        Integer i = TagEnum.valueOf(key).getHash();
                        if (i != null)
                            this.tagHash += (1L << (i - 1));
                    }
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 Enum 값입니다.");
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
            this.minRemainMonth = minRemainMonth;
            this.maxRemainMonth = maxRemainMonth;
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

    @Getter @Setter
    @Builder
    @AllArgsConstructor
    public static class StringSearch {
        @NotNull(message = "페이지 수를 입력 해 주세요.")
        @PositiveOrZero(message = "페이지 수는 0 혹은 양수여야 합니다.")
        private Integer page;

        @NotNull(message = "사이즈 수를 입력 해 주세요.")
        @Positive(message = "사이즈 수는 양수여야 합니다.")
        private Integer size;

        @NotNull(message = "위도를 입력 해 주세요.")
        private Double latitude;

        @NotNull(message = "경도를 입력 해 주세요.")
        private Double longitude;

        @NotBlank(message = "검색어를 입력 해 주세요.")
        private String query;

        @Min(value = 0, message = "거리는 0km 보다 크거나 같아야 합니다.") @Max(value = 20000, message = "거리는 20km (20000m) 보다 작거나 같아야 합니다.")
        @NotNull(message = "거리를 입력 하여야 합니다.")
        private Double maxDistance;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Info {
        // @NotNull: 유저 인증이 붙으면 수정
        User seller;
        String mainImage;

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

        @Min(value = 0)
        Integer remainingNumber;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent
        LocalDate expiryDate;

        public Ticket toEntity() {
            if (this.isMembership && this.expiryDate == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "멤버쉽이라면 만료 일자 (expiryDate) 가 필요합니다.");
            } else if (!this.isMembership && this.remainingNumber == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "멤버쉽이 아니라면, 남은 횟수 (remainingNumber) 가 필요합니다.");
            }

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
                    .mainImage(this.mainImage)
                    .expiryDate(this.expiryDate)
                    .build();
        }
    }
}
