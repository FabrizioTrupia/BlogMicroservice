package it.cgmconsulting.mscategory.controller;

import it.cgmconsulting.mscategory.payload.request.CategoryRequest;
import it.cgmconsulting.mscategory.payload.request.PostCategoryAssociationRequest;
import it.cgmconsulting.mscategory.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/v1")
    public ResponseEntity<?> createCategory(@RequestParam @NotBlank @Size(max=50, min=3) String categoryName){
        return categoryService.createCategory(categoryName);
    }

    @PutMapping("/v1")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CategoryRequest request){
        return categoryService.updateCategory(request);
    }

    @PutMapping("/v2")
    public ResponseEntity<?> postCategoryAssociation(@RequestBody @Valid PostCategoryAssociationRequest request){
        return categoryService.postCategoryAssociation(request);
    }

    @GetMapping("/v1")
    public ResponseEntity<?> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/v2")
    public ResponseEntity<?> getAllVisibleCategories(){
        return categoryService.getAllVisibleCategories();
    }
}
