package depromeet.batonsearch.domain.tag;

import depromeet.batonsearch.domain.tickettag.TicketTag;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@Table(name = "Tag")
public class Tag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Set<TicketTag> ticketTags;

    public void addTicketTag(TicketTag ticketTag) {
        if (this.ticketTags != null)
            this.ticketTags.add(ticketTag);
    }
}
