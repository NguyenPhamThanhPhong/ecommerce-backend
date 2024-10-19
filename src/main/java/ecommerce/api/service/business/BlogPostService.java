package ecommerce.api.service.business;

import ecommerce.api.dto.blogpost.response.BlogPostDisplayResponse;
import ecommerce.api.dto.blogpost.response.BlogPostResponse;
import ecommerce.api.dto.general.ModificationResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.blogpost.request.BlogPostCreateRequest;
import ecommerce.api.dto.blogpost.request.BlogPostUpdateRequest;
import ecommerce.api.entity.BlogPost;
import ecommerce.api.exception.BadRequestException;
import ecommerce.api.exception.ResourceNotFoundException;
import ecommerce.api.mapper.BlogPostMapper;
import ecommerce.api.repository.IBlogPostRepository;
import ecommerce.api.service.azure.CloudinaryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogPostService {
    private final IBlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final CloudinaryService cloudinaryService;

    public ModificationResponse<UUID> createBlogPost(BlogPostCreateRequest request){
        BlogPost blogPost = blogPostMapper.fromCreateRequestToEntity(request);
        return upsertAndReturnChanges(blogPost, request.getImage(), blogPost.getId());
    }

    public BlogPostResponse getBlogPost(UUID id, boolean includeDeleted) {
        Optional<BlogPost> blogPost;
        if (includeDeleted)
            blogPost = blogPostRepository.findFirstByIdAndDeletedAtIsNull(id);
        else
            blogPost = blogPostRepository.findById(id);
        return blogPost.map(blogPostMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found by id " + id));
    }

    public PaginationDTO<BlogPostDisplayResponse> getBlogPostsPageable(Pageable pageable, boolean includeDeleted) {
        Page<BlogPost> blogPosts;
        if (!includeDeleted)
            blogPosts = blogPostRepository.findAll(pageable);
        else
            blogPosts = blogPostRepository.findAllByDeletedAtIsNotNull(pageable);
        Page<BlogPostDisplayResponse> displayResponses = blogPosts.map(blogPostMapper::fromEntityToDisplayResponse);
        return PaginationDTO.fromPage(displayResponses);
    }

    @Transactional
    public int deleteBlogPost(UUID id, boolean isSoft) {
        if (isSoft)
            return blogPostRepository.updateBlogPostDeletedAt(id);
        else
            return blogPostRepository.deleteBlogPostById(id);
    }

    @Transactional
    public ModificationResponse<UUID> updateBlogPost(BlogPostUpdateRequest request){
        BlogPost blogPost = blogPostMapper.fromUpdateRequestToEntity(request);
        return upsertAndReturnChanges(blogPost, request.getImage(), request.getId());
    }

    private ModificationResponse<UUID> upsertAndReturnChanges(BlogPost blogPost, MultipartFile image, UUID id) throws BadRequestException {
        try{
            if (image != null) {
                String imageUrl = cloudinaryService.uploadFile(image, cloudinaryService.BLOG_DIR, id.toString());
                blogPost.setImageUrl(imageUrl);
            }
        }
        catch (IOException e){
            throw new BadRequestException("Failed to upload image");
        }
        BlogPost result = blogPostRepository.save(blogPost);
        return new ModificationResponse<>(result.getId(), result.getCreatedAt(), Map.of("image_url", result.getImageUrl()));
    }
}
