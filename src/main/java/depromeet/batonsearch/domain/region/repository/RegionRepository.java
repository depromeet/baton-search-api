package depromeet.batonsearch.domain.region.repository;

import depromeet.batonsearch.domain.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {
}
