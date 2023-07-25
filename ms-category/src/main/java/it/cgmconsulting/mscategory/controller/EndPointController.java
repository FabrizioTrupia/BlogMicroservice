package it.cgmconsulting.mscategory.controller;

import it.cgmconsulting.mscategory.payload.response.CategoriesResponse;
import it.cgmconsulting.mscategory.service.CategoryService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8090")
@Validated
public class EndPointController {

    private final CategoryService categoryService;

    @GetMapping("/v0/get-categories-by-post/{postId}")
    public Set<String> getCategoriesByPost(@PathVariable @Min(1) long postId){
        return categoryService.getCategoriesByPost(postId);
    }

    @GetMapping("/v0/get-posts-by-category/{categoryName}")
    public Set<Long> getPostsByCategory(@PathVariable String categoryName){
        return categoryService.findPostsByCategory(categoryName.toUpperCase().trim());
    }

    @GetMapping("/v0/find-all-categories-by-posts")
    public List<CategoriesResponse> findAllCategoriesByPosts(){
        return categoryService.findAllCategoriesByPosts();
    }


}
