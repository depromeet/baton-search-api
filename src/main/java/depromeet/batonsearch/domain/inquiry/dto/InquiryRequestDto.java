package depromeet.batonsearch.domain.inquiry.dto;

import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.dto.UserResponseDto;
import lombok.*;


public class InquiryRequestDto {
    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        private Integer userId;
        private Integer ticketId;
        private String content;
    }
}
