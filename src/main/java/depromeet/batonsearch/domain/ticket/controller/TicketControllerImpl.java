package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/query")
public class TicketControllerImpl implements TicketController {
    private final TicketService ticketService;

    @GetMapping(value = "/all")
    @ResponseBody
    public ResponseEntity<Page<TicketResponseDto.Simple>> findAll(@Valid TicketRequestDto.Search search) {
        return new ResponseEntity<>(ticketService.findAll(search), HttpStatus.OK);
    }
}
