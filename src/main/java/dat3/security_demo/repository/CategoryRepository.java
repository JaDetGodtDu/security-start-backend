package dat3.security_demo.repository;

import dat3.security_demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    public Optional<Category> findByName(String name);
}

