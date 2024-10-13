package ecommerce.api.controller;

import ecommerce.api.dto.blogpost.request.BlogPostCreateRequest;
import ecommerce.api.dto.blogpost.request.BlogPostUpdateRequest;
import ecommerce.api.service.business.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("")
    public ResponseEntity<?> getBlogPosts(@ParameterObject Pageable pageable, @RequestParam(required = false) boolean includeDeleted) {
        return ResponseEntity.ok(blogPostService.getBlogPostsPageable(pageable, includeDeleted));
    }

    @PostMapping("")
    public ResponseEntity<?> createBlogPost(@RequestBody BlogPostCreateRequest blogPostResponse) throws IOException {
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
