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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        Ticket ticket1 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").address("반포동").price(12000).state(TicketState.SALE)
                .createdAt(LocalDateTime.now()).latitude(35.0).longitude(35.0).isMembership(true).remainingNumber(80).build());

        Ticket ticket2 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").address("반포동").price(15000).state(TicketState.SALE)
                .createdAt(LocalDateTime.now()).latitude(35.0).longitude(35.0).isMembership(true).remainingNumber(150).build());

        Ticket ticket3 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").address("반포동").price(18000).state(TicketState.SALE)
                .createdAt(LocalDateTime.now()).latitude(35.0).longitude(35.0).isMembership(true).remainingNumber(220).build());

        Ticket ticket4 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").address("반포동").price(12000).state(TicketState.SALE)
                .createdAt(LocalDateTime.now()).latitude(35.0).longitude(35.0).isMembership(false).remainingNumber(10).build());

        Ticket ticket5 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").address("반포동").price(15000).state(TicketState.SALE)
                .createdAt(LocalDateTime.now()).latitude(35.0).longitude(35.0).isMembership(false).remainingNumber(20).build());

        Ticket ticket6 = ticketRepository.save(Ticket.builder().seller(user).location("반포동").address("반포동").price(18000).state(TicketState.SALE)
                .createdAt(LocalDateTime.now()).latitude(35.0).longitude(35.0).isMembership(false).remainingNumber(30).build());

        Tag tag1 = tagRepository.findById(1).orElseThrow(IllegalAccessError::new);
        Tag tag2 = tagRepository.findById(2).orElseThrow(IllegalAccessError::new);
        Tag tag3 = tagRepository.findById(3).orElseThrow(IllegalAccessError::new);

        ticketTagRepository.save(TicketTag.builder().tag(tag1).ticket(ticket1).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag2).ticket(ticket1).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag2).ticket(ticket2).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag3).ticket(ticket2).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag1).ticket(ticket3).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag3).ticket(ticket3).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag1).ticket(ticket4).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag2).ticket(ticket4).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag2).ticket(ticket5).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag3).ticket(ticket5).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag1).ticket(ticket6).build());
        ticketTagRepository.save(TicketTag.builder().tag(tag3).ticket(ticket6).build());
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

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        Assertions.assertThat(simples.getContent()).allMatch(x -> x.getTags().containsAll(hashSet));
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
        Assertions.assertThat(simples.getContent()).allMatch(x -> x.getPrice() >= 15000L);
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
        Assertions.assertThat(simples.getContent()).allMatch(x -> x.getPrice() <= 15000L);
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
        Assertions.assertThat(simples.getContent()).allMatch(x -> x.getPrice() >= 13000L && x.getPrice() <= 17000L);
    }

    @Test
    @Order(7)
    @Transactional
    void 남은_일자_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .minRemainNumber(100)
                .maxRemainNumber(200)
                .isMembership(true)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        Assertions.assertThat(simples.getContent()).allMatch(x -> (x.getRemainingNumber() >= 100 && x.getRemainingNumber() <= 200) && x.getIsMembership());
    }

    @Test
    @Order(8)
    @Transactional
    void 남은_횟수_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .minRemainNumber(15)
                .maxRemainNumber(25)
                .isMembership(false)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        Assertions.assertThat(simples.getContent()).allMatch(x -> (x.getRemainingNumber() >= 15 && x.getRemainingNumber() <= 25) && !x.getIsMembership());
    }

    @Test
    @Order(9)
    @Transactional
    void 거리_쿼리_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .latitude(34.9)
                .longitude(34.9)
                .maxDistance(100.0)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        Assertions.assertThat(simples.getContent().size()).isEqualTo(5);
    }



    @AfterAll
    @Transactional
    void DB_삭제() {
        ticketTagRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }
}
