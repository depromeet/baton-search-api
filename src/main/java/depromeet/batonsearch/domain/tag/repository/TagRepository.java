package depromeet.batonsearch.domain.tag.repository;

import depromeet.batonsearch.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
}
