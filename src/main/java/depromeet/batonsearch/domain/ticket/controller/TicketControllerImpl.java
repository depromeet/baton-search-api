package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
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
    @PostMapping(value = "/post")
    public ResponseEntity<TicketResponseDto.Simple> save(@Valid @ModelAttribute TicketRequestDto.Info info,
                                                         @RequestParam("tags") Set<String> tags,
                                                         @RequestParam("images") Set<MultipartFile> images) {

        TicketResponseDto.Simple response = ticketService.save(info, tags, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/info/{id}")
    public ResponseEntity<TicketResponseDto.Info> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }
}
