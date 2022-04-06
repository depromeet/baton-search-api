package depromeet.batonsearch.domain.ticket.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class TicketRequestDto {

    @ApiModel(value = "검색 시 필요 파라미터")
    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {

        @ApiModelProperty(value = "페이지 번호")
        private Integer page;

        @ApiModelProperty(value = "페이지 당 조회할 양도권 거래 갯수")
        private Integer size;

        @ApiModelProperty(value = "헬스장 이름")
        private String place;

        @ApiModelProperty(value = "해시 태그, 복수로 값을 받을 수 있음.")
        private List<String> hashtag;

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

        @ApiModelProperty(value = "헬스복 포함")
        private Boolean clothes;

        @ApiModelProperty(value = "사물함 포함 여부")
        private Boolean locker;

        @ApiModelProperty(value = "최대 거리 (km)")
        private Double maxDistance;
    }
}
