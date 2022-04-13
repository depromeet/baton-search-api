package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.Tag.Tag;
import depromeet.batonsearch.domain.Tag.repository.TagRepository;
import depromeet.batonsearch.domain.region.Region;
import depromeet.batonsearch.domain.region.repository.RegionRepository;
import depromeet.batonsearch.domain.ticket.dto.TicketRequestDto;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.ticket.repository.TicketQueryRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.ticket.service.TicketService;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketQueryDslTest {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketQueryRepository ticketQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeAll
    @Transactional
    void DB_초기화() {
        Region region = Region.builder()
                .name("지역")
                .build();

        regionRepository.save(region);

        User user = User.builder()
                .name("유저")
                .nickname("유저")
                .region(region)
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

        Ticket ticket1 = Ticket.builder()
                .seller(user)
                .location("지역1")
                .price(10000)
                .createdAt(new Date())
                .tags(tags1)
                .build();

        Ticket ticket2 = Ticket.builder()
                .seller(user)
                .location("지역2")
                .price(15000)
                .createdAt(new Date())
                .tags(tags2)
                .build();

        Ticket ticket3 = Ticket.builder()
                .seller(user)
                .location("지역3")
                .price(20000)
                .createdAt(new Date())
                .tags(tags3)
                .build();

        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);
        ticketRepository.save(ticket3);
    }

    @AfterAll
    @Transactional
    void DB_삭제() {
        ticketRepository.deleteAll();
        tagRepository.deleteAll();
        userRepository.deleteAll();
        regionRepository.deleteAll();
    }

    @Test
    @Transactional
    void 페이지_번호_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder().build();
        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        simples.getContent().forEach(System.out::println);
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
        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        simples.getContent().forEach(System.out::println);
    }

    @Test
    @Transactional
    void 최소_가격_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .minPrice(15000L)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        simples.getContent().forEach(System.out::println);
    }

    @Test
    @Transactional
    void 최대_가격_테스트() {
        TicketRequestDto.Search search = TicketRequestDto.Search.builder()
                .maxPrice(15000L)
                .build();

        PageRequest request = PageRequest.of(0, 5);

        Page<TicketResponseDto.Simple> simples = ticketQueryRepository.searchAll(search, request);
        simples.getContent().forEach(System.out::println);
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
        simples.getContent().forEach(System.out::println);
    }
}
