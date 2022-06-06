package depromeet.batonsearch.domain.bookmark.repository;

import depromeet.batonsearch.domain.bookmark.Bookmark;
import depromeet.batonsearch.domain.ticket.Ticket;
import depromeet.batonsearch.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Boolean existsBookmarkByTicketAndUser(Ticket ticket, User user);
}
