package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;

@SpringBootTest
public class AllDtoTest {
    @Test
    void TicketResponseDto_테스트() {
        User user = User.builder()
                .nickname("유저")
                .gender(true)
                .build();

        Tag tag1 = Tag.builder().subject("태그1").content("태그1").build();
        Tag tag2 = Tag.builder().subject("태그2").content("태그2").build();

        HashSet<Tag> tags1 = new HashSet<>();
        tags1.add(tag1);
        tags1.add(tag2);

        Random random = new Random();
        
        Ticket ticket = Ticket.builder()
                .seller(user)
                .location("안녕 헬스장")
                .address("강남동")
                .price(random.nextInt(8000) + 12000)
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

        TicketResponseDto.Simple ticketSimple = TicketResponseDto.Simple.of(ticket);
        Assertions.assertThat(ticketSimple).isInstanceOf(TicketResponseDto.Simple.class);
    }
}
