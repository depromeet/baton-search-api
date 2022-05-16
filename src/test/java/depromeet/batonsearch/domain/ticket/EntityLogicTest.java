package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.tag.repository.TagRepository;
import depromeet.batonsearch.domain.ticket.repository.TicketRepository;
import depromeet.batonsearch.domain.tickettag.TicketTag;
import depromeet.batonsearch.domain.tickettag.repository.TicketTagRepository;
import depromeet.batonsearch.domain.user.User;
import depromeet.batonsearch.domain.user.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntityLogicTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TicketTagRepository ticketTagRepository;

    @Test
    @Order(1)
    @Transactional
    void 태그_수정_테스트() {
        User user = userRepository.save(User.builder().id(1234567890).nickname("유저").gender(true).build());

        Tag tag1 = tagRepository.save(Tag.builder().subject("태그1").content("태그1").build());
        Tag tag2 = tagRepository.save(Tag.builder().subject("태그2").content("태그2").build());

        Ticket ticket = Ticket.builder()
                .seller(user)
                .location("안녕 헬스장")
                .address("강남동")
                .price(10000)
                .createdAt(LocalDateTime.now())
                .state(TicketState.SALE)
                .type(TicketType.HEALTH)
                .description("이사가요")
                .transferFee(TicketTransferFee.NONE)
                .latitude(37.49888733403366)
                .longitude(127.0279892656964)
                .isMembership(true)
                .remainingNumber(150)
                .build();

        ticketRepository.save(ticket);
        
        TicketTag ticketTag1 = TicketTag.builder().tag(tag1).ticket(ticket).build();
        ticketTagRepository.save(ticketTag1);
        assertThat(ticket.getTagHash()).isEqualTo(1L << (tag1.getId() - 1));
        assertThat(ticket.getTicketTags()).contains(ticketTag1);

        TicketTag ticketTag2 = TicketTag.builder().tag(tag2).ticket(ticket).build();
        ticketTagRepository.save(ticketTag2);
        assertThat(ticket.getTagHash()).isEqualTo((1L << (tag1.getId() - 1)) + (1L << (tag2.getId() - 1)));
        assertThat(ticket.getTicketTags()).contains(ticketTag2);

        ticketTagRepository.delete(ticketTag1);
        assertThat(ticket.getTagHash()).isEqualTo(1L << (tag2.getId() - 1));
        assertThat(ticket.getTicketTags()).doesNotContain(ticketTag1);

        ticketTagRepository.delete(ticketTag2);
        assertThat(ticket.getTagHash()).isEqualTo(0);
        assertThat(ticket.getTicketTags()).doesNotContain(ticketTag2);
    }
}