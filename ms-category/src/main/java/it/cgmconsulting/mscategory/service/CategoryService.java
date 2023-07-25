package it.cgmconsulting.mscategory.service;

import it.cgmconsulting.mscategory.entity.Category;
import it.cgmconsulting.mscategory.entity.CategoryId;
import it.cgmconsulting.mscategory.entity.CategoryPosts;
import it.cgmconsulting.mscategory.exception.ResourceNotFoundException;
import it.cgmconsulting.mscategory.payload.request.CategoryRequest;
import it.cgmconsulting.mscategory.payload.request.PostCategoryAssociationRequest;
import it.cgmconsulting.mscategory.payload.response.CategoriesResponse;
import it.cgmconsulting.mscategory.repository.CategoryPostsRepository;
import it.cgmconsulting.mscategory.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryPostsRepository categoryPostsRepository;


    public ResponseEntity<?> createCategory(String categoryName) {
        categoryName = categoryName.toUpperCase().trim();
        if(categoryRepository.existsByCategoryName(categoryName))
            return new ResponseEntity("Category already present", HttpStatus.BAD_REQUEST);
        categoryRepository.save(new Category(categoryName));
        return new ResponseEntity("Category successfully created", HttpStatus.CREATED);
    }

    // aggiornamento categoria relativamente al nome ed alla visibilità
    @Transactional
    public ResponseEntity<?> updateCategory(CategoryRequest request){
        String newCategoryName = request.getNewCategoryName().toUpperCase().trim();
        if(categoryRepository.existsByCategoryNameAndIdNot(newCategoryName, request.getCategoryId()))
            return new ResponseEntity<>("Category already present", HttpStatus.BAD_REQUEST);
        Category cat = findCategory(request.getCategoryId());
        cat.setCategoryName(newCategoryName);
        cat.setVisible(request.isNewVisibility());
        return new ResponseEntity<>("Category updated succesfully", HttpStatus.OK);
    }

    public ResponseEntity<?> getAllCategories(){
        return new ResponseEntity(categoryRepository.getAllCategories(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllVisibleCategories(){
        return new ResponseEntity(categoryRepository.getAllVisibleCategories(), HttpStatus.OK);
    }

    protected Category findCategory(long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", categoryId)
        );
        return category;
    }


    public ResponseEntity<?> postCategoryAssociation(PostCategoryAssociationRequest request) {
        LocalDateTime start = LocalDateTime.now();
        long postId = request.getPostId();
        List<Category> categories = categoryRepository.getAllByIdAndVisibleTrue(request.getCategoryIds());
        List<CategoryPosts> list = new ArrayList<CategoryPosts>();

        categoryPostsRepository.deleteCategoryPost(postId);

        for(Category cat : categories){
            categoryPostsRepository.insertCategoryPost(postId, cat.getId());
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("### TEMPO DI ESECUZIONE: "+start.until(end, ChronoUnit.MILLIS));

        return new ResponseEntity("Categories updated for post "+postId, HttpStatus.OK);
    }

    public Set<String> getCategoriesByPost(long postId) {
        return categoryPostsRepository.findCategoriesByPostId(postId);
    }

    public Set<Long> findPostsByCategory(String categoryName){
        return categoryPostsRepository.findPostsByCategory(categoryName);
    }

    public List<CategoriesResponse> findAllCategoriesByPosts(){

        List<CategoryPosts> list = categoryPostsRepository.findAllCategoriesByPosts();
        List<CategoriesResponse> cr = new ArrayList<>();
        Set<String> categories = new HashSet<>();

        /*
        *   1   torino
        *   1   portogallo
        *   1   roma
        *   1   italia
        *   2   europa
        *   2   portogallo
        */

        /* soluzione con stream: lenta */
        /*
        Map<Long, List<CategoryPosts>> l2 = list.stream()
                .collect(Collectors.groupingBy(c -> c.getCategoryId().getPostId()));

        l2.keySet().forEach(l -> cr.add(new CategoriesResponse(l,
                        l2.get(l)
                                .stream().map(cat -> cat.getCategoryId().getCategory().getCategoryName()).collect(Collectors.toSet())
                )
        ));
        */


        Set<Long> postIds = list.stream().map(p -> p.getCategoryId().getPostId()).collect(Collectors.toSet());
        for(long postId : postIds){
            for(CategoryPosts c : list){
                if(c.getCategoryId().getPostId() == postId)
                    categories.add(c.getCategoryId().getCategory().getCategoryName());
            }
            cr.add(new CategoriesResponse(postId, categories));
            categories = new HashSet<>();
        }


        /* GABRIEL
        if(!list.isEmpty()){
            cr.add(new CategoriesResponse(list.get(0).getCategoryId().getPostId(),
                    new HashSet<>(Arrays.asList(list.get(0).getCategoryId().getCategory().getCategoryName()))));
            for (int i = 1; i < list.size(); i++){
                if(list.get(i).getCategoryId().getPostId() == cr.get(cr.size()-1).getPostId()){
                    cr.get(cr.size()-1).addCategory(
                            list.get(i).getCategoryId().getCategory().getCategoryName()
                    );
                } else {
                    cr.add(new CategoriesResponse(list.get(i).getCategoryId().getPostId(),
                            new HashSet<>(Arrays.asList(list.get(i).getCategoryId().getCategory().getCategoryName()))));
                }
            }
        }
        */

        return cr;
    }
}



/*

        */