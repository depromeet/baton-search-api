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
@Table(name = "user_report")
public class UserReport {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Column
    private String contents;
}