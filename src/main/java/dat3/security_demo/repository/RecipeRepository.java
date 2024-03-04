package dat3.security_demo.repository;

import dat3.security_demo.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository  extends JpaRepository<Recipe,Integer> {
    List<Recipe> findByCategoryName(String categoryName);
}

