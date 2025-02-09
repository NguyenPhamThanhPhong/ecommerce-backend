package ecommerce.api.service.business;

import ecommerce.api.config.property.CloudinaryProperties;
import ecommerce.api.dto.DataChangeResponse;
import ecommerce.api.dto.blogpost.response.BlogPostResponse;
import ecommerce.api.dto.general.ModificationResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.blogpost.request.BlogPostCreateRequest;
import ecommerce.api.dto.blogpost.request.BlogPostUpdateRequest;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.BlogPost;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.BlogPostMapper;
import ecommerce.api.repository.IBlogPostRepository;
import ecommerce.api.service.azure.CloudinaryService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BlogPostService {
    private final IBlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final CloudinaryService cloudinaryService;
    private final CloudinaryProperties cloudinaryProperties;

    public DataChangeResponse createBlogPost(BlogPostCreateRequest request) {
        BlogPost blogPost = blogPostMapper.fromCreateRequestToEntity(request);
        return upsertAndReturnChanges(blogPost, request.getImage());
    }

    public BlogPostResponse getBlogPost(long code, boolean includeDeleted) {
        Optional<BlogPost> blogPost;
        if (includeDeleted)
            blogPost = blogPostRepository.findFirstByCodeAndDeletedAtIsNull(code);
        else
            blogPost = blogPostRepository.findByCode(code);
        return blogPost.map(blogPostMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found by id " + code));
    }

    public PaginationDTO<BlogPostResponse> search(Set<SearchSpecification> searchSpec, Pageable pageable) {
        Specification<BlogPost> spec = DynamicSpecificationUtils.buildSpecification(searchSpec);
        Page<BlogPost> blogPosts = blogPostRepository.findAll(spec, pageable);
        var res = blogPosts.map(blogPostMapper::fromEntityToResponse);
        return PaginationDTO.fromPage(res);
    }

    @Transactional
    public int deleteBlogPost(UUID id, boolean isSoft) {
        if (isSoft)
            return blogPostRepository.updateBlogPostDeletedAt(id);
        else
            return blogPostRepository.deleteBlogPostById(id);
    }

    @Transactional
    public DataChangeResponse updateBlogPost(BlogPostUpdateRequest request) {
        BlogPost blogPost = blogPostMapper.fromUpdateRequestToEntity(request);
        return upsertAndReturnChanges(blogPost, request.getImage());
    }

    private DataChangeResponse upsertAndReturnChanges(BlogPost blogPost, MultipartFile image) throws BadRequestException {
        try {
            if (image != null) {
                String imageUrl = cloudinaryService.uploadFile(image, cloudinaryProperties.getBlogDir(),
                        blogPost.getId().toString());
                blogPost.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image");
        }
        BlogPost result = blogPostRepository.save(blogPost);
        return new DataChangeResponse(result.getId(), result.getImageUrl());
    }
}
