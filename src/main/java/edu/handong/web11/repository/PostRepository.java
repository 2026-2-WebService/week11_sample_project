package edu.handong.web11.repository;

import edu.handong.web11.domain.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override @EntityGraph(attributePaths = "attachments")
    List<Post> findAll(Sort sort);
    @Override @EntityGraph(attributePaths = "attachments")
    Optional<Post> findById(Long id);
}
