package depromeet.batonsearch.domain.buy;

import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@Table(name = "Buy")
public class Buy {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "bought_at")
    private Date boughtAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
