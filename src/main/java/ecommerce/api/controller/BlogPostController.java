package ecommerce.api.controller;

import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.blogpost.request.BlogPostCreateRequest;
import ecommerce.api.dto.blogpost.request.BlogPostUpdateRequest;
import ecommerce.api.dto.blogpost.response.BlogPostDisplayResponse;
import ecommerce.api.dto.general.PaginationDTO;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.entity.user.Account;
import ecommerce.api.service.business.BlogPostService;
import ecommerce.api.utils.DynamicSpecificationUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/blog-posts")
@RequiredArgsConstructor
public class BlogPostController {
    private final BlogPostService blogPostService;

    @GetMapping("{id}")
    public ResponseEntity<?> getBlogPost(@PathVariable UUID id, @RequestParam(required = false) boolean includeDeleted) {
        return ResponseEntity.ok(blogPostService.getBlogPost(id, includeDeleted));
    }

    @PostMapping("/filtered-paginated-info")
    public ResponseEntity<?> createTempAccount(
            @ParameterObject Pageable pageable,
            @RequestBody Set<SearchSpecification> searchSpecs) {
        PaginationDTO<BlogPostDisplayResponse> accounts = blogPostService.search(searchSpecs, pageable);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("")
    @PreAuthorize("hasRole(T(ecommerce.api.constants.AuthRoleConstants).ROLE_ADMIN)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> createBlogPost(@ModelAttribute @Valid BlogPostCreateRequest blogPostResponse) throws IOException {
        return ResponseEntity.ok(blogPostService.createBlogPost(blogPostResponse));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable UUID id, @RequestParam(required = false) boolean isSoft) {
        return ResponseEntity.ok(blogPostService.deleteBlogPost(id, isSoft));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateBlogPost(@PathVariable UUID id, @RequestBody BlogPostUpdateRequest blogPostResponse) throws IOException {
        blogPostResponse.setId(id);
        return ResponseEntity.ok(blogPostService.updateBlogPost(blogPostResponse));
    }
}
