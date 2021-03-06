package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticketimage.dto.TicketImageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface TicketController {
    ResponseEntity<Page<TicketResponseDto.Simple>> findAll(TicketRequestDto.Search search);
    ResponseEntity<Long> countSearch(TicketRequestDto.Search search);
    ResponseEntity<Page<TicketResponseDto.Simple>> stringSearch(TicketRequestDto.StringSearch search);
    ResponseEntity<TicketResponseDto.Simple> save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images);
    ResponseEntity<TicketResponseDto.Info> findById(Integer id, Double latitude, Double longitude);
    ResponseEntity<String> deleteById(Integer id);
    ResponseEntity<String> deleteImageById(Integer id);
    ResponseEntity<List<TicketImageResponseDto>> ticketImagePost(Integer id, Set<MultipartFile> images);
    ResponseEntity<TicketResponseDto.Info> ticketPut(Integer id, TicketRequestDto.Put data);
}