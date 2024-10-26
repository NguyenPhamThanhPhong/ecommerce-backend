package ecommerce.api.service.business;


import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.entity.product.Category;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.mapper.CategoryMapper;
import ecommerce.api.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::fromEntityToResponse).toList();
    }

    public CategoryResponse getCategoryById(UUID id) {
        return categoryMapper.fromEntityToResponse(categoryRepository.findById(id).orElse(null));
    }

    public CategoryResponse upsertCategory(Category category) {
        return categoryMapper.fromEntityToResponse(categoryRepository.save(category));
    }

    public int deleteCategory(UUID id) {
        return categoryRepository.deleteCategoryById(id);
    }


    public CategoryResponse getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findCategoryByName(name);
        Category real = category.orElseThrow(()->new BadRequestException("...Not exist"));
        return categoryMapper.fromEntityToResponse(real);
    }

}
