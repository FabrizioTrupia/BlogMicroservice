package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping("/v2")
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequest request, @RequestHeader("userId") long authorId){
        return postService.createPost(request, authorId);
    }

    @PutMapping("/v2/{postId}")
    public ResponseEntity<?> updatePost(@RequestBody @Valid PostRequest request, @RequestHeader("userId") long authorId, @PathVariable long postId){
        return postService.updatePost(request, authorId, postId);
    }

    @PutMapping("/v1/publish/{postId}")
    public ResponseEntity<?> publishPost(@PathVariable long postId, @RequestParam(required = false) LocalDateTime publishedAt){
        return postService.publishPost(postId, publishedAt);
    }

    @GetMapping("/v0/{postId}")
    public ResponseEntity<?> getPost(@PathVariable long postId){
        return postService.getPost(postId);
    }

    @GetMapping("/v0/posts")
    public ResponseEntity<?> getAllPublishedPost(HttpServletRequest request){
        return postService.getAllPublishedPosts();
    }

    @GetMapping("/v0/find-posts-by-category") // ordinati per data adi pubblicazione DESC
    public ResponseEntity<?> findPostsByCategory(@RequestParam String categoryName){
        return postService.findPostsByCategory(categoryName);
    }



}
