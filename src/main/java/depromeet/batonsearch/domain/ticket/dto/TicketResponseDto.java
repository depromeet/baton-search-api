package depromeet.batonsearch.domain.ticket.dto;

import depromeet.batonsearch.domain.Tag.Tag;
import depromeet.batonsearch.domain.ticket.Ticket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TicketResponseDto {
    @ApiModel(value = "양도권 정보 축약형 모델")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        @ApiModelProperty(name = "양도권 ID")
        private Integer id;

        @ApiModelProperty(name = "판매자 닉네임")
        private String sellerNickname;

        @ApiModelProperty(name = "위치")
        private String location;

        @ApiModelProperty(name = "가격")
        private Integer price;

        @ApiModelProperty(name = "판매 정보 생성 일자")
        private Date createAt;

        @ApiModelProperty(name = "소유 태그 리스트")
        private List<String> tags;

        public static Simple of(Ticket ticket) {
            return Simple.builder()
                    .id(ticket.getId())
                    .sellerNickname(ticket.getSeller().getNickname())
                    .location(ticket.getLocation())
                    .price(ticket.getPrice())
                    .createAt(ticket.getCreatedAt())
                    .tags(ticket.getTags().stream().map(Tag::getSubject).collect(Collectors.toList()))
                    .build();
        }
    }
}
