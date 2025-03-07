package dat3.security_demo.configuration;
import dat3.security_demo.entity.Recipe;
import dat3.security_demo.repository.RecipeRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdjustAllRecipes implements ApplicationRunner {
    RecipeRepository recipeRepository;

    public AdjustAllRecipes(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Recipe> allRecipes = recipeRepository.findAll();
        allRecipes.forEach((recipe -> {
            recipe.setOwner("user2");
        }));
        recipeRepository.saveAll(allRecipes);
    }
}

