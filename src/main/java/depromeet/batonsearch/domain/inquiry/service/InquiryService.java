package depromeet.batonsearch.domain.inquiry.service;

import depromeet.batonsearch.domain.inquiry.Inquiry;
import depromeet.batonsearch.domain.inquiry.dto.InquiryRequestDto;
import depromeet.batonsearch.domain.inquiry.dto.InquiryResponseDto;
import depromeet.batonsearch.domain.inquiry.repository.InquiryRepository;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {
    private final TicketRepository ticketRepository;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final HttpServletRequest request;

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

    private User getUserByUserIdInHeader() {
        return userRepository.getById(getUserIdInHeader());
    }

    @Transactional
    public InquiryResponseDto.Simple postInquiry(InquiryRequestDto.Post data) {
        Ticket ticket = ticketRepository.findById(data.getTicketId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "양도권을 찾을 수 없습니다.")
        );

        Inquiry inquiry = inquiryRepository.save(
                Inquiry.builder()
                        .user(getUserByUserIdInHeader())
                        .ticket(ticket)
                        .content(data.getContent())
                        .isRead(false)
                        .build()
        );

        return InquiryResponseDto.Simple.of(inquiry);
    }

    @Transactional(readOnly = true)
    public List<InquiryResponseDto.Simple> getReceivedInquiries() {
        return inquiryRepository.findByUser(getUserByUserIdInHeader())
                .stream().map(InquiryResponseDto.Simple::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InquiryResponseDto.Simple> getSendInquiries() {
        return inquiryRepository.findByReceivedUser(getUserByUserIdInHeader())
                .stream().map(InquiryResponseDto.Simple::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InquiryResponseDto.Info findInquiryById(Integer id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의를 찾지 못했습니다.")
        );
        Integer userId = getUserIdInHeader();

        if (userId != inquiry.getUser().getId() && userId != inquiry.getTicket().getSeller().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        return InquiryResponseDto.Info.of(inquiry);
    }
}
