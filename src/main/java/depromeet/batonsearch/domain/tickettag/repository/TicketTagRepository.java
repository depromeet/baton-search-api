package depromeet.batonsearch.domain.tickettag.repository;

import depromeet.batonsearch.domain.tickettag.TicketTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTagRepository extends JpaRepository<TicketTag, Integer> {
}
