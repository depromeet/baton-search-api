package depromeet.batonsearch.domain.ticket.service;

import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    final private TicketRepository ticketRepository;
    final private TicketQueryRepository ticketQueryRepository;

    @Override
    public Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search) {
        return ticketQueryRepository.searchAll(search, PageRequest.of(search.getPage(), search.getSize()));
    }

    @Override
    public TicketResponseDto.Simple save(TicketRequestDto.Info info) {
        Ticket ticket = ticketRepository.save(info.toEntity());
        return TicketResponseDto.Simple.of(ticket);
    }
}
