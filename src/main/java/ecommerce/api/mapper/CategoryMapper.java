package ecommerce.api.mapper;


import ecommerce.api.dto.category.request.CategoryCreateRequest;
import ecommerce.api.dto.category.request.CategoryUpdateRequest;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.entity.product.Category;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    public Category fromCreateRequestToEntity(CategoryCreateRequest categoryCreateRequest);
    public Category fromUpdateRequestToEntity(CategoryUpdateRequest categoryUpdateRequest);

    public CategoryResponse fromEntityToResponse(Category category);
    public Category fromResponseToEntity(CategoryResponse categoryResponse);
}
