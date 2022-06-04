package depromeet.batonsearch.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import depromeet.batonsearch.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter @Setter
public class UserResponseDto {
    private Integer id;
    private String name;
    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate createdOn;

    static public UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .createdOn(user.getCreatedOn())
                .build();
    }
}
