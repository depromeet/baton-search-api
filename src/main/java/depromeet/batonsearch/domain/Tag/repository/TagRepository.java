package depromeet.batonsearch.domain.Tag.repository;

import depromeet.batonsearch.domain.Tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}
