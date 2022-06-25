package depromeet.batonsearch.domain.report;

import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "ticket_report")
public class TicketReport {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Column
    private String contents;
}
