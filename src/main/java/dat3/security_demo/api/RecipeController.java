package dat3.security_demo.api;

import dat3.security_demo.dto.RecipeDto;
import dat3.security_demo.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getAllRecipes(@RequestParam(required = false) String category) {
        if(category != null) {
            System.out.println("Category: " + category);
        }
        return recipeService.getAllRecipes(category);
    }

    @GetMapping(path ="/{id}")
    public RecipeDto getRecipeById(@PathVariable int id) {
        return recipeService.getRecipeById(id);
    }

    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping
    public RecipeDto addRecipe(@RequestBody RecipeDto request, Principal p) {
        String username = p.getName();
        return recipeService.addRecipe(request, username);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PutMapping(path = "/{id}")
    public RecipeDto addRecipe(@RequestBody RecipeDto request,@PathVariable int id) {
        return recipeService.editRecipe(request,id);
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteRecipe(@PathVariable int id) {
        return recipeService.deleteRecipe(id);
    }
}

