package depromeet.batonsearch.domain.fcm.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FCMRequestDto {
    private Integer userId;
    private String title;
    private String body;
}
