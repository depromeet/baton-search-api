package depromeet.batonsearch.domain.bookmark.repository;

import depromeet.batonsearch.domain.bookmark.Bookmark;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    @Query(value = "select b.id from Bookmark b where b.ticket = :ticket and b.user = :user")
    Optional<Integer> findBookmarkIdByTicketAndUser(@Param("ticket") Ticket ticket, @Param("user") User user);
}
