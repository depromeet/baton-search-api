package depromeet.batonsearch.domain.report.repository;

import depromeet.batonsearch.domain.report.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Integer> {
}
