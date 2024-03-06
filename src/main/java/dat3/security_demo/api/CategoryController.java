package dat3.security_demo.api;

import dat3.security_demo.dto.CategoryDto;
import dat3.security_demo.dto.RecipeDto;
import dat3.security_demo.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<String> getAllCategories() {
        return categoryService.getAllCategories();
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public CategoryDto addCategory(@RequestBody CategoryDto request) {

        return categoryService.addRecipe(request);
    }
}

