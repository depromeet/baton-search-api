package depromeet.batonsearch.domain.ticket.dto;

import com.querydsl.core.annotations.QueryProjection;
import depromeet.batonsearch.domain.ticket.Ticket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
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
        @ApiModelProperty(name = "양도권 ID")
        private Integer id;

        @ApiModelProperty(name = "위치")
        private String location;

        @ApiModelProperty(name = "가격")
        private Integer price;

        @ApiModelProperty(name = "판매 정보 생성 일자")
        private Date createAt;

        @ApiModelProperty(name = "소유 태그 리스트")
        private Set<String> tags;

        private Boolean isMembership;
        private Date expiryDate;
        private Integer remainingNumber;

        @Builder
        @QueryProjection
        public Simple(Integer id, String location, Integer price, Date createAt, Boolean isMembership, Date expiryDate, Integer remainingNumber, Long tagHash) {
            this.id = id;
            this.location = location;
            this.price = price;
            this.createAt = createAt;
            this.isMembership = isMembership;
            this.expiryDate = expiryDate;
            this.remainingNumber = remainingNumber;
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
                .tagHash(ticket.getTagHash())
                .createAt(ticket.getCreatedAt())
                .isMembership(ticket.getIsMembership())
                .expiryDate(ticket.getExpiryDate())
                .remainingNumber(ticket.getRemainingNumber())
                .build();
        }
    }
}
