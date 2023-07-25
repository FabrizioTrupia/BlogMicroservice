package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.response.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, long postId);

    @Query(value="SELECT new it.cgmconsulting.mspost.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.content, " +
            "p.publishedAt, " +
            "p.authorId) " +
            "FROM Post p " +
            "WHERE p.id = :postId AND (p.publishedAt IS NOT NULL AND p.publishedAt <= :now)")
    PostResponse getPost(@Param("postId") long postId, @Param("now") LocalDateTime now);


    @Query(value="SELECT new it.cgmconsulting.mspost.payload.response.PostListResponseComplete(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.authorId) " +
            "FROM Post p " +
            "WHERE p.publishedAt IS NOT NULL AND p.publishedAt <= :now")
    List<PostListResponseComplete> getAllPublishedPosts(@Param("now") LocalDateTime now);


    @Query(value="SELECT new it.cgmconsulting.mspost.payload.response.PostListResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview" +
            ") FROM Post p " +
            "WHERE (p.publishedAt IS NOT NULL AND p.publishedAt <= :now) " +
            "AND p.id IN (:ids) " +
            "ORDER BY p.publishedAt DESC")
    List<PostListResponse> getPostsByCategory(@Param("ids") Set<Long> ids, @Param("now") LocalDateTime now);


    @Query(value="SELECT new it.cgmconsulting.mspost.payload.response.UserPostResponse(" +
            "p.authorId," +
            "p.id" +
            ") FROM Post p " +
            "ORDER BY p.authorId")
    List<UserPostResponse> userPost();
}
