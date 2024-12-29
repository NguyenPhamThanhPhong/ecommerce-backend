package ecommerce.api.service.business;


import ecommerce.api.config.property.CloudinaryProperties;
import ecommerce.api.dto.category.request.CategoryCreateRequest;
import ecommerce.api.dto.category.request.CategoryUpdateRequest;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.product.Category;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.CategoryMapper;
import ecommerce.api.repository.ICategoryRepository;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final CategoryMapper categoryMapper;
    private final CloudinaryProperties cloudinaryProperties;

    public PaginationDTO<CategoryResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<Category> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<Category> categories = categoryRepository.findAll(spec, pageable);
        Page<CategoryResponse> responses = categories.map(categoryMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(responses);
    }

    public CategoryResponse getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("CATEGORY NOT FOUND"));
        return categoryMapper.fromEntityToResponse(category);
    }

    public CategoryResponse insert(CategoryCreateRequest request) throws IOException {
        Category category = categoryMapper.fromCreateRequestToEntity(request);
        if (request.getImage() != null) {
            String imageUrl = cloudinaryService.uploadFile(request.getImage(),
                    cloudinaryProperties.getCategoryDir(), category.getId().toString());
            category.setImageUrl(imageUrl);
        }
        return this.upsert(category);
    }

    public CategoryResponse update(CategoryUpdateRequest request) throws IOException {
        Category category = categoryMapper.fromUpdateRequestToEntity(request);
        if (request.getImage() != null) {
            cloudinaryService.deleteFile(category.getId().toString());
            String imageUrl = cloudinaryService.uploadFile(request.getImage(),
                    cloudinaryProperties.getCategoryDir(), category.getId().toString());
            category.setImageUrl(imageUrl);
        }
        return this.upsert(category);
    }

    public CategoryResponse upsert(Category category) {
        return categoryMapper.fromEntityToResponse(categoryRepository.save(category));
    }

    @Transactional(timeout = 15)
    public int deleteCategory(UUID id) {
        try {
            return categoryRepository.deleteCategoryById(id);
        } catch (DataIntegrityViolationException ex) {
            return categoryRepository.updateCategoryDeletedAt(id);
        }
    }


}
