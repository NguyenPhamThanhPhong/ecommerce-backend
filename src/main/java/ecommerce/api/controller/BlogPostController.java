package ecommerce.api.controller;

import ecommerce.api.dto.blogpost.request.BlogPostCreateRequest;
import ecommerce.api.dto.blogpost.request.BlogPostUpdateRequest;
import ecommerce.api.dto.blogpost.response.BlogPostDisplayResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.service.business.BlogPostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/blog-posts")
@RequiredArgsConstructor
public class BlogPostController {
    private final BlogPostService blogPostService;

    @GetMapping("{code}")
    public ResponseEntity<?> getBlogPost(@PathVariable long code, @RequestParam(required = false) boolean includeDeleted) {
        return ResponseEntity.ok(blogPostService.getBlogPost(code, includeDeleted));
    }

    @PostMapping("/searches")
    public ResponseEntity<?> search(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<BlogPostDisplayResponse> accounts = blogPostService.search(searchSpecs, pageable);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping(value = "",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_ADMIN)")
    public ResponseEntity<?> createBlogPost(@ModelAttribute @Valid BlogPostCreateRequest request) throws IOException {
        return ResponseEntity.ok(blogPostService.createBlogPost(request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable UUID id, @RequestParam(required = false) boolean isSoft) {
        return ResponseEntity.ok(blogPostService.deleteBlogPost(id, isSoft));
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateBlogPost(@PathVariable UUID id, @RequestBody BlogPostUpdateRequest blogPostResponse) throws IOException {
        blogPostResponse.setId(id);
        return ResponseEntity.ok(blogPostService.updateBlogPost(blogPostResponse));
    }

}
