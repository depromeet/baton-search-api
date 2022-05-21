package depromeet.batonsearch.domain.ticket.controller;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface TicketController {
    ResponseEntity<Page<TicketResponseDto.Simple>> findAll(TicketRequestDto.Search search);
    ResponseEntity<TicketResponseDto.Simple> save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images);
    ResponseEntity<TicketResponseDto.Info> findById(Integer id, Double latitude, Double longitude);
    ResponseEntity<String> deleteById(Integer id);
}