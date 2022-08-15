package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.service.TicketService;
import depromeet.batonsearch.domain.ticketimage.dto.TicketImageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ticket")
public class TicketControllerImpl implements TicketController {
    final private TicketService ticketService;

    @GetMapping(value = "/query")
    public ResponseEntity<Page<TicketResponseDto.Simple>> findAll(@Valid TicketRequestDto.Search search) {
        return new ResponseEntity<>(ticketService.findAll(search), HttpStatus.OK);
    }

    @GetMapping(value = "/string_query")
    public ResponseEntity<Page<TicketResponseDto.Simple>> stringSearch(@Valid TicketRequestDto.StringSearch search) {
        return new ResponseEntity<>(ticketService.stringSearch(search), HttpStatus.OK);
    }

    @GetMapping(value = "/count_query")
    public ResponseEntity<Long> countSearch(TicketRequestDto.Search search) {
        return new ResponseEntity<>(ticketService.countSearch(search), HttpStatus.OK);
    }

    @PostMapping(value = "/post")
    public ResponseEntity<TicketResponseDto.Simple> save(@Valid @ModelAttribute TicketRequestDto.Info info,
                                                         @RequestParam(value = "tags", required = false) Set<String> tags,
                                                         @RequestParam(value = "images", required = false) Set<MultipartFile> images) {

        TicketResponseDto.Simple response = ticketService.save(info, tags, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/info/{id}")
    public ResponseEntity<TicketResponseDto.Info> findById(@PathVariable("id") Integer id,
                                                           @RequestParam("latitude") Double latitude,
                                                           @RequestParam("longitude") Double longitude) {
        return ResponseEntity.ok(ticketService.findById(id, latitude, longitude));
    }

    @DeleteMapping(value = "/info/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ticketService.deleteById(id));
    }

    @DeleteMapping(value = "/image/{id}")
    public ResponseEntity<String> deleteImageById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ticketService.deleteImageById(id));
    }

    @PostMapping(value = "/info/{id}")
    public ResponseEntity<List<TicketImageResponseDto>> ticketImagePost(@PathVariable("id") Integer id,
                                                                        @RequestParam("images") Set<MultipartFile> images) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.ticketImagePost(id, images));
    }

    @PutMapping(value = "/info/{id}")
    public ResponseEntity<TicketResponseDto.Info> ticketPut(@PathVariable Integer id,
                                                            @Valid @RequestBody TicketRequestDto.Put data) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ticketService.modify(id, data));
    }

    @GetMapping(value = "/{ticketId}/inquiries_count")
    public ResponseEntity<Integer> getInquiriesCount(@PathVariable Integer ticketId) {
        return ResponseEntity.ok(ticketService.countInquiries(ticketId));
    }
}
