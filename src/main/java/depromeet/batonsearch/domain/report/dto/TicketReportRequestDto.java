package depromeet.batonsearch.domain.report.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TicketReportRequestDto {

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotNull
        private Integer ticketId;

        @NotBlank
        private String content;
    }
}
