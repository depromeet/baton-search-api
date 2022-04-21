package depromeet.batonsearch.domain.ticket.service;

import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface TicketService {
    Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search);
}
