package depromeet.batonsearch.domain.report.service;

import depromeet.batonsearch.domain.report.TicketReport;
import depromeet.batonsearch.domain.report.UserReport;
import depromeet.batonsearch.domain.report.dto.TicketReportRequestDto;
import depromeet.batonsearch.domain.report.dto.UserReportRequestDto;
import depromeet.batonsearch.domain.report.repository.TicketReportRepository;
import depromeet.batonsearch.domain.report.repository.UserReportRepository;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.ticket.service.TicketService;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketReportRepository ticketReportRepository;
    private final UserReportRepository userReportRepository;
    private final TicketService ticketService;
    private final HttpServletRequest request;

    @Override
    @Transactional
    public Integer postTicketReport(TicketReportRequestDto.Post data) {
        User reporter = getUserByUserIdInHeader();
        Ticket ticket = ticketRepository.findById(data.getTicketId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "양도권이 존재하지 않습니다.")
        );

        TicketReport ticketReport = TicketReport.builder()
                .reporter(reporter)
                .ticket(ticket)
                .contents(data.getContent())
                .build();
        return ticketReportRepository.save(ticketReport).getId();
    }

    @Override
    @Transactional
    public Integer postUserReport(UserReportRequestDto.Post data) {
        User reporter = getUserByUserIdInHeader();
        User user = userRepository.findById(data.getUserId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );

        UserReport userReport = UserReport.builder()
                .reporter(reporter)
                .user(user)
                .contents(data.getContent())
                .build();
        return userReportRepository.save(userReport).getId();
    }

    private User getUserByUserIdInHeader() {
        return userRepository.getById(getUserIdInHeader());
    }

    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("Remote-User");

        if (userIdString == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }
}
