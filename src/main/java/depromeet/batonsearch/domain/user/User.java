package depromeet.batonsearch.domain.user;

import depromeet.batonsearch.domain.bookmark.Bookmark;
import depromeet.batonsearch.domain.tag.Tag;
import depromeet.batonsearch.domain.usertag.UserTag;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@Table(name = "User")
public class User {
    @Id
    private Integer id;

    @Column
    private String nickname;

    @Column(nullable = false)
    private Boolean gender;

    @OneToMany(mappedBy = "id")
    private Set<UserTag> userTags = new HashSet<>();

    @OneToMany(mappedBy = "id")
    private Set<Bookmark> bookmarks = new HashSet<>();
}
