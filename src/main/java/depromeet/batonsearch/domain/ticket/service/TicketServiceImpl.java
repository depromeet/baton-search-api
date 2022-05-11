package depromeet.batonsearch.domain.ticket.service;

import depromeet.batonsearch.domain.tag.StaticTag;
import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.tag.repository.TagRepository;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.tickettag.TicketTag;
import depromeet.batonsearch.domain.tickettag.repository.TicketTagRepository;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    final private TicketRepository ticketRepository;
    final private TicketQueryRepository ticketQueryRepository;
    final private UserRepository userRepository;
    final private TagRepository tagRepository;
    final private TicketTagRepository ticketTagRepository;
    final private HttpServletRequest request;

    @Override
    public Page<TicketResponseDto.Simple> findAll(TicketRequestDto.Search search) {
        return ticketQueryRepository.searchAll(search, PageRequest.of(search.getPage(), search.getSize()));
    }

    @Override
    public TicketResponseDto.Simple save(TicketRequestDto.Info info, Set<String> tags, Set<MultipartFile> images) {
        // User Check
        User user = userRepository.getById(getUserIdInHeader());
        info.setSeller(user);

        // Ticket Save
        Ticket ticket = info.toEntity();
        ticketRepository.save(ticket);

        // Tag add
        tags.stream()
            .map(x -> StaticTag.tagToKey.getOrDefault(x, -1))
            .filter(x -> x != -1)
            .map(tagRepository::getById)
            .forEach(tag -> ticketTagRepository.save(
                    TicketTag.builder().ticket(ticket).tag(tag).build()
            ));

        return TicketResponseDto.Simple.of(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponseDto.Info findById(Integer id) {
        return TicketResponseDto.Info.of(
                ticketRepository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket Not Found")
            )
        );
    }

    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("userId");
        Integer userId;

        if (userIdString == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }

        return userId;
    }
}
