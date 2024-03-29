package depromeet.batonsearch.domain.user;

import depromeet.batonsearch.domain.bookmark.Bookmark;
import depromeet.batonsearch.domain.buy.Buy;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "created_on")
    private LocalDate createdOn;

    @Column
    private String image;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToMany(mappedBy = "id")
    private Set<Bookmark> bookmarks;

    @OneToMany(mappedBy = "id")
    private Set<Buy> buys;

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
