package ecommerce.api.controller;
import ecommerce.api.dto.category.request.CategoryCreateRequest;
import ecommerce.api.dto.category.request.CategoryUpdateRequest;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.product.Category;
import ecommerce.api.service.business.CategoryService;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllCategories(@PathVariable UUID id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/filtered-paginated-info")
    public ResponseEntity<?> createTempAccount(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<CategoryResponse> categories = categoryService.search(searchSpecs, pageable);
        return ResponseEntity.ok(categories);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCategory(@ModelAttribute CategoryCreateRequest request) throws IOException {
        CategoryResponse res = categoryService.insert(request);
        return ResponseEntity.ok(res);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCategory(@ModelAttribute CategoryUpdateRequest request) throws IOException {
        CategoryResponse res = categoryService.update(request);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable UUID id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}
