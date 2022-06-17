package depromeet.batonsearch.domain.ticket.service;

import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticketimage.dto.TicketImageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface TicketService {
    Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search);
    Page<TicketResponseDto.Simple> stringSearch(TicketRequestDto.StringSearch search);
    Long countSearch(TicketRequestDto.Search search);
    TicketResponseDto.Simple save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images);
    TicketResponseDto.Info modify(Integer id, TicketRequestDto.Put data);
    TicketResponseDto.Info findById(Integer id, Double latitude, Double longitude);
    List<TicketImageResponseDto> ticketImagePost(Integer id, Set<MultipartFile> images);
    String deleteById(Integer id);
}
