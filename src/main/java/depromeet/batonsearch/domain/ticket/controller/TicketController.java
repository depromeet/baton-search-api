package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface TicketController {
    public ResponseEntity<Page<TicketResponseDto.Simple>> findAll(TicketRequestDto.Search search);
}