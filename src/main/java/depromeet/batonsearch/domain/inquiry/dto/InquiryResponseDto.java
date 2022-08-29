package depromeet.batonsearch.domain.inquiry.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import depromeet.batonsearch.domain.inquiry.Inquiry;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.user.dto.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class InquiryResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private Integer id;
        private UserResponseDto user;
        private TicketResponseDto.Inquiry ticket;
        private String content;
        private Boolean isRead;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public static Simple of(Inquiry inquiry) {
            return Simple.builder()
                    .id(inquiry.getId())
                    .user(UserResponseDto.of(inquiry.getUser()))
                    .ticket(TicketResponseDto.Inquiry.of(inquiry.getTicket()))
                    .content(inquiry.getContent())
                    .isRead(inquiry.getIsRead())
                    .createdAt(inquiry.getCreatedAt())
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
        private Integer id;
        private UserResponseDto.Inquiry user;
        private TicketResponseDto.Inquiry ticket;
        private String content;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public static Info of(Inquiry inquiry) {
            return Info.builder()
                    .id(inquiry.getId())
                    .user(UserResponseDto.Inquiry.of(inquiry.getUser()))
                    .ticket(TicketResponseDto.Inquiry.of(inquiry.getTicket()))
                    .content(inquiry.getContent())
                    .createdAt(inquiry.getCreatedAt())
                    .build();
        }
    }
}
