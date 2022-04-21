package depromeet.batonsearch.domain.ticket;

import depromeet.batonsearch.domain.tickettag.TicketTag;
import depromeet.batonsearch.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Ticket")
public class Ticket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @Column
    private String location;

    @Column
    private Integer price;

    @Column(name = "created_at")
    private Date createdAt;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "tag_hash")
    private Long tagHash;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Set<TicketTag> ticketTags = new HashSet<>();

    @Builder
    public Ticket(User seller, User buyer, String location, Integer price, Date createdAt, Double latitude, Double longitude) {
        this.tagHash = 0L;
        this.seller = seller;
        this.buyer = buyer;
        this.location = location;
        this.price = price;
        this.createdAt = createdAt;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void addTicketTag(TicketTag ticketTag) {
        if (this.ticketTags != null)
            this.ticketTags.add(ticketTag);
        this.tagHash |= 1L << (ticketTag.getTag().getId() - 1);
    }

    public void removeTicketTag(TicketTag ticketTag) {
        if (this.ticketTags != null)
            this.ticketTags.remove(ticketTag);

        this.tagHash -= 1L << (ticketTag.getTag().getId() - 1);
    }
}
