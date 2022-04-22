package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.ticket.dto.TicketResponseDto;
import depromeet.batonsearch.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static depromeet.batonsearch.domain.tag.StaticTag.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;

@SpringBootTest
public class AllDtoTest {
    @Test
    void TicketResponseDto_테스트() {
        User user = User.builder()
                .id(1)
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
                .location("반포동")
                .price(random.nextInt(8000) + 12000)
                .createdAt(new Date())
                .latitude(35.0)
                .longitude(35.0)
                .isMembership(true)
                .expiryDate(new Date(2022, 10, 01))
                .build();

        TicketResponseDto.Simple ticketSimple = TicketResponseDto.Simple.of(ticket);
        System.out.println("tagToKey = " + tagToKey);
        System.out.println("keyToTag = " + keyToTag);
        System.out.println("ticket = " + ticket);
        System.out.println("ticketSimple = " + ticketSimple);
    }
}
