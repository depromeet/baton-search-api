package depromeet.batonsearch.domain.report.controller;

import depromeet.batonsearch.domain.report.dto.TicketReportRequestDto;
import depromeet.batonsearch.domain.report.dto.UserReportRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReportController {
    ResponseEntity<Integer> postTicketReport(TicketReportRequestDto.Post data);
    ResponseEntity<Integer> postUserReport(UserReportRequestDto.Post data);
}
