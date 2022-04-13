package depromeet.batonsearch.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import depromeet.batonsearch.domain.Tag.Tag;
import depromeet.batonsearch.domain.ticket.Ticket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketResponseDto {
    @ApiModel(value = "양도권 정보 축약형 모델")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Simple {
        @ApiModelProperty(name = "양도권 ID")
        private Integer id;

        @ApiModelProperty(name = "판매자 닉네임")
        private Integer sellerId;

        @ApiModelProperty(name = "위치")
        private String location;

        @ApiModelProperty(name = "가격")
        private Integer price;

        @ApiModelProperty(name = "판매 정보 생성 일자")
        private Date createAt;

        @ApiModelProperty(name = "소유 태그 리스트")
        private Set<Tag> tags;

        @QueryProjection
        public Simple(Integer id, Integer sellerId, String location, Integer price, Date createAt, Set<Tag> tags) {
            this.id = id;
            this.sellerId = sellerId;
            this.location = location;
            this.price = price;
            this.createAt = createAt;
            this.tags = tags;
        }

        public static Simple of(Ticket ticket) {
            return Simple.builder()
                    .id(ticket.getId())
                    .sellerId(ticket.getSeller().getId())
                    .location(ticket.getLocation())
                    .price(ticket.getPrice())
                    .createAt(ticket.getCreatedAt())
                    .tags(ticket.getTags())
                    .build();
        }
    }
}
