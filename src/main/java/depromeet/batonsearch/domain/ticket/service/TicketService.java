package depromeet.batonsearch.domain.ticket.service;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TicketService {
    Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search);
    TicketResponseDto.Simple save(TicketRequestDto.Info info);
}
