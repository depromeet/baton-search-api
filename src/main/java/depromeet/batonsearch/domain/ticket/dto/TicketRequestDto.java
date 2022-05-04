package depromeet.batonsearch.domain.ticket.dto;


import depromeet.batonsearch.domain.ticket.*;
import lombok.*;

import java.util.Set;

import static depromeet.batonsearch.domain.tag.StaticTag.tagToKey;

public class TicketRequestDto {

    @Getter @Setter
    public static class Search {
        private Integer page;
        private Integer size;
        private String place;
        private Long tagHash;
        private Double latitude;
        private Double longitude;
        private String town;
        private Long minPrice;
        private Long maxPrice;
        private Integer minRemainNumber;
        private Integer maxRemainNumber;
        private Integer minRemainDay;
        private Integer maxRemainDay;
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
        private Double maxDistance;

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
}
