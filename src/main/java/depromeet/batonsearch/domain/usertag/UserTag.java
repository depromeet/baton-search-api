package depromeet.batonsearch.domain.usertag;

import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor
@Table(name = "UserTag")
public class UserTag {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
