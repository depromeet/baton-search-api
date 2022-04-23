package depromeet.batonsearch.domain.ticket.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Set;

import static depromeet.batonsearch.domain.tag.StaticTag.tagToKey;

public class TicketRequestDto {

    @ApiModel(value = "검색 시 필요 파라미터")
    @Getter @Setter
    public static class Search {
        @ApiModelProperty(value = "페이지 번호")
        private Integer page;

        @ApiModelProperty(value = "페이지 당 조회할 양도권 거래 갯수")
        private Integer size;

        @ApiModelProperty(value = "헬스장 이름")
        private String place;

        private Long tagHash;

        @ApiModelProperty(value = "위도")
        private Double latitude;

        @ApiModelProperty(value = "경도")
        private Double longitude;

        @ApiModelProperty(value = "동네")
        private String town;

        @ApiModelProperty(value = "최소 금액")
        private Long minPrice;

        @ApiModelProperty(value = "최대 금액")
        private Long maxPrice;

        @ApiModelProperty(value = "최소 남은 기간")
        private Integer minRemainDay;

        @ApiModelProperty(value = "최대 남은 기간")
        private Integer maxRemainDay;

        @ApiModelProperty(value = "최소 남은 횟수")
        private Integer minRemainNumber;

        @ApiModelProperty(value = "최대 남은 횟수")
        private Integer maxRemainNumber;

        @ApiModelProperty(value = "헬스복 포함")
        private Boolean clothes;

        @ApiModelProperty(value = "사물함 포함 여부")
        private Boolean locker;

        @ApiModelProperty(value = "최대 거리 (km)")
        private Double maxDistance;

        @Builder
        public Search(Integer page, Integer size, String place, Set<String> hashtag, Double latitude, Double longitude, String town, Long minPrice, Long maxPrice, Integer minRemainDay, Integer maxRemainDay, Boolean clothes, Boolean locker, Double maxDistance, Integer minRemainNumber, Integer maxRemainNumber) {
            this.tagHash = 0L;

            if (hashtag != null)
                for (String key: hashtag) {
                    this.tagHash += (1L << (tagToKey.get(key) - 1));
                }

            this.page = page;
            this.size = size;
            this.place = place;
            this.latitude = latitude;
            this.longitude = longitude;
            this.town = town;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.minRemainDay = minRemainDay;
            this.maxRemainDay = maxRemainDay;
            this.minRemainNumber = minRemainNumber;
            this.maxRemainNumber = maxRemainNumber;
            this.clothes = clothes;
            this.locker = locker;
            this.maxDistance = maxDistance;
        }
    }
}
