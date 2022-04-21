package depromeet.batonsearch.domain.usertag.repository;

import depromeet.batonsearch.domain.usertag.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTagRepository extends JpaRepository<UserTag, Integer> {
}
