package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ticket")
public class TicketControllerImpl implements TicketController {
    final private TicketService ticketService;

    @Transactional(readOnly = true)
    @GetMapping(value = "/query")
    public ResponseEntity<Page<TicketResponseDto.Simple>> findAll(@Valid TicketRequestDto.Search search) {
        return new ResponseEntity<>(ticketService.findAll(search), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value="/post")
    public ResponseEntity<TicketResponseDto.Simple> save(@Valid @RequestBody TicketRequestDto.Info info) {
        TicketResponseDto.Simple response = ticketService.save(info);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
