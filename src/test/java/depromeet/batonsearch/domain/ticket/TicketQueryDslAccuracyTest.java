package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.tag.repository.TagRepository;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.tickettag.TicketTag;
import depromeet.batonsearch.domain.tickettag.repository.TicketTagRepository;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import depromeet.batonsearch.domain.usertag.repository.UserTagRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketQueryDslAccuracyTest {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketTagRepository ticketTagRepository;

    @Autowired
    private UserTagRepository userTagRepository;

    @Autowired
    private TicketQueryRepository ticketQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @Order(1)
    @Transactional
    @Rollback(value = false)
    void DB_초기화() {
        User user = userRepository.save(User.builder().id(1).nickname("유저").gender(true).build());

        Ticket ticket1 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").price(12000)
                .createdAt(new Date()).latitude(35.0).longitude(35.0).build());

        Ticket ticket2 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").price(15000)
                .createdAt(new Date()).latitude(35.0).longitude(35.0).build());

        Ticket ticket3 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").price(18000)
                .createdAt(new Date()).latitude(35.0).longitude(35.0).build());

        Tag tag1 = tagRepository.findById(1).orElseThrow(IllegalAccessError::new);
        Tag tag2 = tagRepository.findById(2).orElseThrow(IllegalAccessError::new);
        Tag tag3 = tagRepository.findById(3).orElseThrow(IllegalAccessError::new);

        ticketTagRepository.save(TicketTag.builder().tag(tag1).ticket(ticket1).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag2).ticket(ticket1).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag2).ticket(ticket2).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag3).ticket(ticket2).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag1).ticket(ticket3).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag3).ticket(ticket3).build());
    }

    @Test
    @Order(2)
    @Transactional(readOnly = true)
    void 데이터_확인() {
        List<User> userList = userRepository.findAll();
        System.out.println("userList = " + userList);

        List<Tag> tagList = tagRepository.findAll();
        System.out.println("tagList = " + tagList);

        List<Ticket> ticketList = ticketRepository.findAll();
        System.out.println("ticketList = " + ticketList);
    }

    @Test
    @Order(2)
    @Transactional(readOnly = true)
    void 페이지_번호_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder().build();
        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);

        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }

    @Test
    @Order(3)
    @Transactional(readOnly = true)
    void 태그_테스트() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("태그1");
        hashSet.add("태그2");

        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .hashtag(hashSet)
                .build();

        System.out.println("search.getTagHash() = " + search.getTagHash());
        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);

        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }

    @Test
    @Order(4)
    @Transactional(readOnly = true)
    void 최소_가격_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .minPrice(15000L)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }

    @Test
    @Order(5)
    @Transactional
    void 최대_가격_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .maxPrice(15000L)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }

    @Test
    @Order(6)
    @Transactional
    void 사이_가격_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .minPrice(13000L)
                .maxPrice(17000L)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }


    @AfterAll
    @Transactional
    void DB_삭제() {
        ticketTagRepository.deleteAll();
        userTagRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }
}