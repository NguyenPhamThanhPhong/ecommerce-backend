package ecommerce.api.mapper;

import ecommerce.api.dto.blogpost.request.BlogPostCreateRequest;
import ecommerce.api.dto.blogpost.response.BlogPostResponse;
import ecommerce.api.dto.blogpost.request.BlogPostUpdateRequest;
import ecommerce.api.entity.BlogPost;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogPostMapper {

    BlogPostMapper INSTANCE = Mappers.getMapper(BlogPostMapper.class);

    BlogPost fromCreateRequestToEntity(BlogPostCreateRequest request);
    BlogPost fromUpdateRequestToEntity(BlogPostUpdateRequest request);

    BlogPostResponse fromEntityToResponse(BlogPost entity);
}
