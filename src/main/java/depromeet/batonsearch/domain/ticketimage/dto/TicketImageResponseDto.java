package depromeet.batonsearch.domain.ticketimage.dto;

import depromeet.batonsearch.domain.ticketimage.TicketImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
@AllArgsConstructor
public class TicketImageResponseDto {
    private Integer id;
    private String url;
    private String thumbnailUrl;
    private Boolean isMain;

    static public TicketImageResponseDto of(TicketImage ticketImage) {
        return TicketImageResponseDto.builder()
                    .id(ticketImage.getId())
                    .url(ticketImage.getUrl())
                    .thumbnailUrl(ticketImage.getThumbnailUrl())
                    .isMain(ticketImage.getIsMain())
                    .build();
    }
}
