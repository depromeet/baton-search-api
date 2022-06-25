package depromeet.batonsearch.domain.report.controller;

import depromeet.batonsearch.domain.report.dto.TicketReportRequestDto;
import depromeet.batonsearch.domain.report.dto.UserReportRequestDto;
import depromeet.batonsearch.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/report")
public class ReportControllerImpl implements ReportController {
    private final ReportService reportService;

    @PostMapping("/ticket")
    public ResponseEntity<Integer> postTicketReport(@Valid @RequestBody TicketReportRequestDto.Post data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.postTicketReport(data));
    }

    @PostMapping("/user")
    public ResponseEntity<Integer> postUserReport(@Valid @RequestBody UserReportRequestDto.Post data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reportService.postUserReport(data));
    }
}
