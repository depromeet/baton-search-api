package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.tag.repository.TagRepository;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.tickettag.repository.TicketTagRepository;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import depromeet.batonsearch.domain.usertag.repository.UserTagRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketQueryDslSpeedTest {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketQueryRepository ticketQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TicketTagRepository ticketTagRepository;

    @Autowired
    private UserTagRepository userTagRepository;

    @BeforeAll
    @Transactional
    void DB_초기화() {
        User user = User.builder()
                .id(1)
                .nickname("유저")
                .gender(true)
                .build();

        userRepository.save(user);

        Tag tag1 = Tag.builder().subject("태그1").content("태그1").build();
        Tag tag2 = Tag.builder().subject("태그2").content("태그2").build();
        Tag tag3 = Tag.builder().subject("태그3").content("태그3").build();

        tagRepository.save(tag1);
        tagRepository.save(tag2);
        tagRepository.save(tag3);


        HashSet<Tag> tags1 = new HashSet<>();
        tags1.add(tag1);
        tags1.add(tag2);

        HashSet<Tag> tags2 = new HashSet<>();
        tags2.add(tag2);
        tags2.add(tag3);

        HashSet<Tag> tags3 = new HashSet<>();
        tags3.add(tag1);
        tags3.add(tag3);

        List<HashSet<Tag>> tagsList = new ArrayList<>();
        tagsList.add(tags1);
        tagsList.add(tags2);
        tagsList.add(tags3);

        List<Ticket> tickets = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 1000; i++) {
            Ticket ticket = Ticket.builder()
                    .seller(user)
                    .location("반포동")
                    .price(random.nextInt(8000) + 12000)
                    .createdAt(LocalDateTime.now())
                    .latitude(35.0)
                    .longitude(35.0)
                    .isMembership(false)
                    .remainingNumber(random.nextInt(100))
                    .build();
            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);

        System.out.println("DB 초기화 종료");
    }

    @AfterAll
    @Transactional
    void DB_삭제() {
        ticketTagRepository.deleteAll();
        userTagRepository.deleteAll();
        ticketRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void 페이지_번호_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder().build();
        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);

        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }

    @Test
    @Transactional
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
    @Transactional
    void 최소_가격_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .minPrice(15000L)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        System.out.println("simples.getContent().get(0).toString() = " + simples.getContent().get(0).toString());
    }

    @Test
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
}
