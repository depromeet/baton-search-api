package depromeet.batonsearch.domain.fcm.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FCMRequestDto {
    private String targetToken;
    private String title;
    private String body;
}
