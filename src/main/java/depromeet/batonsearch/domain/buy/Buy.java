package depromeet.batonsearch.domain.buy;

import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Buy")
public class Buy {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column
    private LocalDateTime date;
}
