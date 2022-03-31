package depromeet.batonsearch.domain.ticket;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Integer id;

    @Column(nullable = false, name = "seller_id")
    private Integer sellerId;

    @Column
    private String location;

    @Column
    private String tags;

    @Column
    private Integer price;

    @Column(name = "created_at")
    private Date createdAt;
}
