package depromeet.batonsearch.domain.tickettag;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.ticket.Ticket;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TicketTag")
public class TicketTag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public TicketTag(Ticket ticket, Tag tag) {
        this.ticket = ticket;
        this.tag = tag;
    }

    @PrePersist
    private void onPrePersist() {
        this.ticket.addTicketTag(this);
    }

    @PreRemove
    private void onPreRemove() {
        this.ticket.removeTicketTag(this);
    }
}
