package dat3.security_demo.service;

import dat3.security_demo.dto.RecipeDto;
import dat3.security_demo.entity.Category;
import dat3.security_demo.entity.Recipe;
import dat3.security_demo.repository.CategoryRepository;
import dat3.security_demo.repository.RecipeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;

    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RecipeDto> getAllRecipes(String category) {
        List<Recipe> recipes = category == null ? recipeRepository.findAll() : recipeRepository.findByCategoryName(category);
        List<RecipeDto> recipeResponses = recipes.stream().map((r) -> new RecipeDto(r,false)).toList();
        return recipeResponses;
    }

    public RecipeDto getRecipeById(int idInt) {
        Recipe recipe = recipeRepository.findById(idInt).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        return new RecipeDto(recipe,false);
    }

    public RecipeDto addRecipe(RecipeDto request, String username) {
        request.setOwner(username);
        if (request.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot provide the id for a new recipe");
        }
        Category category = categoryRepository.findByName(request.getCategory()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing categories are allowed"));
        Recipe newRecipe = new Recipe();

        updateRecipe(newRecipe, request, category);
        recipeRepository.save(newRecipe);
        return new RecipeDto(newRecipe,false);
    }
    private void updateRecipe(Recipe original, RecipeDto r, Category category) {
        original.setName(r.getName());
        original.setInstructions(r.getInstructions());
        original.setIngredients(r.getIngredients());
        original.setThumb(r.getThumb());
        original.setYouTube(r.getYouTube());
        original.setSource(r.getSource());
        original.setCategory(category);
        original.setOwner(r.getOwner());
    }

    public RecipeDto editRecipe(RecipeDto request, int id) {
        if (request.getId() != id) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot change the id of an existing recipe");
        }
        Category category = categoryRepository.findByName(request.getCategory()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Only existing categories are allowed"));

        checkRoles(request.getOwner());

        Recipe recipeToEdit = recipeRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        updateRecipe(recipeToEdit,request, category);
        recipeRepository.save(recipeToEdit);
        return new RecipeDto(recipeToEdit,false);
    }

    public ResponseEntity deleteRecipe(int id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        checkRoles(recipe.getOwner());
        recipeRepository.delete(recipe);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    private static void checkRoles(String owner) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        boolean isAdmin = roles.contains("ADMIN");
        String name = auth.getName();
        if(!isAdmin && !name.equals(owner)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You may only edit/delete your own recipes");
        }
    }

}

