package ecommerce.api.controller;
import ecommerce.api.dto.category.request.CategoryCreateRequest;
import ecommerce.api.dto.category.request.CategoryUpdateRequest;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.entity.product.Category;
import ecommerce.api.mapper.CategoryMapper;
import ecommerce.api.service.business.CategoryService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<?> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name){
        CategoryResponse category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(@ModelAttribute CategoryCreateRequest request){
        Category category = categoryMapper.fromCreateRequestToEntity(request);
        return ResponseEntity.ok(categoryService.upsertCategory(category));

    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCategory(@ModelAttribute CategoryUpdateRequest request){
        Category category = categoryMapper.fromUpdateRequestToEntity(request);

        return ResponseEntity.ok(categoryService.upsertCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
