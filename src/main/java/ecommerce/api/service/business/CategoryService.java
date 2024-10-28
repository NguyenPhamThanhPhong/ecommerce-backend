package ecommerce.api.service.business;


import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.entity.product.Category;
import ecommerce.api.mapper.CategoryMapper;
import ecommerce.api.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public List<CategoryResponse> getAllCategories() {
        return this.categoryRepository.findAllParentCategories().stream().map(
                c -> {
                    CategoryResponse categoryResponse = categoryMapper.fromEntityToResponse(c);
                    setChildren(categoryResponse);
                    return categoryResponse;
                }).toList();
    }

    private void setChildren(CategoryResponse parent) {
        List<CategoryResponse> children = categoryRepository.findAllChildren(parent.getId()).stream().map(categoryMapper::fromEntityToResponse).toList();
        if(!children.isEmpty()) {
            parent.setChildren(children);
            for(CategoryResponse child : children) {
                setChildren(child);
            }
        }
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
        return categoryMapper.fromEntityToResponse(categoryRepository.findCategoryByName(name));
    }

}
