package depromeet.batonsearch.domain.report.repository;

import depromeet.batonsearch.domain.report.TicketReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketReportRepository extends JpaRepository<TicketReport, Integer> {
}
