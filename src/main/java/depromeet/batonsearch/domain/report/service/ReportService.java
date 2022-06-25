package depromeet.batonsearch.domain.report.service;

import depromeet.batonsearch.domain.report.dto.TicketReportRequestDto;
import depromeet.batonsearch.domain.report.dto.UserReportRequestDto;

public interface ReportService {
    Integer postTicketReport(TicketReportRequestDto.Post data);
    Integer postUserReport(UserReportRequestDto.Post data);
}
