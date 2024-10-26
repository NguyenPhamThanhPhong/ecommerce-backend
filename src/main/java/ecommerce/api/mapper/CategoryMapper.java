package ecommerce.api.mapper;


import ecommerce.api.dto.category.request.CategoryCreateRequest;
import ecommerce.api.dto.category.request.CategoryUpdateRequest;
import ecommerce.api.dto.category.response.CategoryResponse;
import ecommerce.api.entity.product.Category;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring" , builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    Category fromCreateRequestToEntity(CategoryCreateRequest categoryCreateRequest);
    Category fromUpdateRequestToEntity(CategoryUpdateRequest categoryUpdateRequest);

    CategoryResponse fromEntityToResponse(Category category);
    Category fromResponseToEntity(CategoryResponse categoryResponse);
}
