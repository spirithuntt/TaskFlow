package taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskflow.entities.Tags;

public interface TagsRepository extends JpaRepository<Tags, Long> {

}
