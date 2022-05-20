package depromeet.batonsearch.domain.user.dto;

import depromeet.batonsearch.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter @Setter
public class UserResponseDto {
    private Integer id;
    private String nickname;
    private Boolean gender;

    static public UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .build();
    }
}
