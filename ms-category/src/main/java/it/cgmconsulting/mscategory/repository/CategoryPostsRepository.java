package it.cgmconsulting.mscategory.repository;

import it.cgmconsulting.mscategory.entity.CategoryId;
import it.cgmconsulting.mscategory.entity.CategoryPosts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CategoryPostsRepository extends JpaRepository<CategoryPosts, CategoryId> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO category_posts (post_id, category_id) VALUES (:postId, :categoryId)", nativeQuery = true)
    void insertCategoryPost(long postId, long categoryId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM category_posts WHERE post_id = :postId", nativeQuery = true)
    void deleteCategoryPost(long postId);

    @Query(value = "SELECT cp.categoryId.category.categoryName FROM CategoryPosts cp " +
            "WHERE cp.categoryId.postId = :postId " +
            "AND cp.categoryId.category.visible = true")
    Set<String> findCategoriesByPostId(@Param("postId") long postId);

    @Query(value = "SELECT cp FROM CategoryPosts cp " +
            "WHERE cp.categoryId.category.visible = true ORDER BY cp.categoryId.postId")
    List<CategoryPosts> findAllCategoriesByPosts();

    @Query(value="SELECT cat.categoryId.postId " +
            "FROM CategoryPosts cat " +
            "WHERE cat.categoryId.category.categoryName = :categoryName " +
            "AND cat.categoryId.category.visible=true ")
    Set<Long> findPostsByCategory(@Param("categoryName") String categoryName);
}
