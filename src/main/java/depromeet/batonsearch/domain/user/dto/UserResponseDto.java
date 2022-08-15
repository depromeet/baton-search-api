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
    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate createdOn;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .createdOn(user.getCreatedOn())
                .image(user.getImage())
                .build();
    }

    @Builder
    @AllArgsConstructor
    @Getter @Setter
    public static class Inquiry {
        private Integer id;
        private String name;
        private String nickname;
        private String image;
        private String phoneNumber;

        public static Inquiry of(User user) {
            return Inquiry.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .image(user.getImage())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        }
    }
}
