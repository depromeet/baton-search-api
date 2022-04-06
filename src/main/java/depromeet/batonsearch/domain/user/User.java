package depromeet.batonsearch.domain.user;

import depromeet.batonsearch.domain.Tag.Tag;
import depromeet.batonsearch.domain.region.Region;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@Table(name = "User")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean gender;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToMany
    @JoinTable(name = "UserTag", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}
