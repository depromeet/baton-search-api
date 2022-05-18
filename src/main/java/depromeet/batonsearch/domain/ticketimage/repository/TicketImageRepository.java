package depromeet.batonsearch.domain.ticketimage.repository;

import depromeet.batonsearch.domain.ticketimage.TicketImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketImageRepository extends JpaRepository<TicketImage, Integer> {
}
