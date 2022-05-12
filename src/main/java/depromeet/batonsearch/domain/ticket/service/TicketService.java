package depromeet.batonsearch.domain.ticket.service;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface TicketService {
    Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search);
    TicketResponseDto.Simple save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images);
    TicketResponseDto.Info findById(Integer id);
    String deleteById(Integer id);
}
