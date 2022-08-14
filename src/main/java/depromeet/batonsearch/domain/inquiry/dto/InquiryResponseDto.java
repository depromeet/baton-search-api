package depromeet.batonsearch.domain.inquiry.dto;

import depromeet.batonsearch.domain.inquiry.Inquiry;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.user.dto.UserResponseDto;
import lombok.*;

import java.util.Collection;
import java.util.stream.Collectors;

public class InquiryResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private UserResponseDto user;
        private TicketResponseDto.Inquiry ticket;
        private String content;
        private Boolean isRead;

        public static Simple of(Inquiry inquiry) {
            return Simple.builder()
                    .user(UserResponseDto.of(inquiry.getUser()))
                    .ticket(TicketResponseDto.Inquiry.of(inquiry.getTicket()))
                    .content(inquiry.getContent())
                    .isRead(inquiry.getIsRead())
                    .build();
        }

        public static Collection<Simple> of(Collection<Inquiry> inquiries) {
            return inquiries.stream().map(Simple::of).collect(Collectors.toList());
        }
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private UserResponseDto.Inquiry user;
        private TicketResponseDto.Inquiry ticket;
        private String content;

        public static Info of(Inquiry inquiry) {
            return Info.builder()
                    .user(UserResponseDto.Inquiry.of(inquiry.getUser()))
                    .ticket(TicketResponseDto.Inquiry.of(inquiry.getTicket()))
                    .content(inquiry.getContent())
                    .build();
        }
    }
}
