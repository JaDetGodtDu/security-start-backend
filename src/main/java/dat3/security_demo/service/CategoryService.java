package dat3.security_demo.service;

import dat3.security_demo.dto.CategoryDto;
import dat3.security_demo.dto.RecipeDto;
import dat3.security_demo.entity.Category;
import dat3.security_demo.entity.Recipe;
import dat3.security_demo.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getAllCategories() {
        List<Category> categories =  categoryRepository.findAll();
        //Convert from list of Categories to DTO-type, list of Strings
        return categories.stream().map((c)->new String(c.getName())).toList();
    }
    public CategoryDto addCategory(CategoryDto request) {
        if (request.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }
        Category newCategory = new Category();
        updateCategory(newCategory, request);
        categoryRepository.save(newCategory);
        return new CategoryDto(newCategory, false);
    }
    private void updateCategory(Category original, CategoryDto category) {

        original.setName(category.getName());
    }

}

