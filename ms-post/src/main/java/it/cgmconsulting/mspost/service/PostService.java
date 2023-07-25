package it.cgmconsulting.mspost.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.exception.ResourceNotFoundException;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.*;
import it.cgmconsulting.mspost.repository.PostRepository;
import it.cgmconsulting.mspost.utils.EndPoints;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public ResponseEntity<?> createPost(PostRequest request, long authorId) {
        if(postRepository.existsByTitle(request.getTitle()))
            return new ResponseEntity("Title already in use", HttpStatus.BAD_REQUEST);
        Post p = fromRequestToEntity(request, authorId);
        return new ResponseEntity(postRepository.save(p), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updatePost(PostRequest request, long authorId, long postId) {
        // verificare titolo
        if(postRepository.existsByTitleAndIdNot(request.getTitle(), postId))
            return new ResponseEntity("Title already in use", HttpStatus.BAD_REQUEST);
        Post p = findPost(postId);
        p.setTitle(request.getTitle());
        p.setOverview(request.getOverview());
        p.setContent(request.getContent());
        p.setAuthorId(authorId);
        p.setPublishedAt(null);
        return new ResponseEntity(p, HttpStatus.OK);
    }

    private Post fromRequestToEntity(PostRequest request, long authorId){
        return new Post(request.getTitle(), request.getOverview(), request.getContent(), authorId);
    }

    protected Post findPost(long postId){
        Post p = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId)
        );
        return p;
    }

    @Transactional
    public ResponseEntity<?> publishPost(long postId, LocalDateTime publishedAt) {
        if(publishedAt != null && publishedAt.isBefore(LocalDateTime.now()))
            return new ResponseEntity("You cannot publish the post in the past", HttpStatus.BAD_REQUEST);
        if(publishedAt == null)
            publishedAt = LocalDateTime.now();
        Post p = findPost(postId);
        p.setPublishedAt(publishedAt);
        return new ResponseEntity("The post '"+p.getTitle()+"' has been published", HttpStatus.OK);
    }

    public ResponseEntity<?> getPost(long postId) {
        PostResponse postResponse = getPostById(postId);
        if(postResponse == null)
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);
        return new ResponseEntity(postResponse, HttpStatus.OK);
    }

    public PostResponse getPostById(long postId){
        PostResponse p = postRepository.getPost(postId, LocalDateTime.now());
        if(p != null) {
            String author = getAuthor(p.getAuthorId());
            p.setAuthor(author);
            Set<String> categories = getCategories(postId);
            p.setCategories(categories);
            double avg = getAverage(postId);
            p.setAvgRating(avg);
        }
        return p;
    }

    public double getAverage(long postId){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY+EndPoints.MS_RATING+"/v0/get-avg-by-post/"+postId;
        try{
            return restTemplate.getForObject(resourceUrl, Double.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return 0d;
        }
    }

    private String getAuthor(long userId){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY+EndPoints.MS_AUTH+"/v0/"+userId;
        try {
           return restTemplate.getForObject(resourceUrl, String.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
    }

    public Set<String> getCategories(long postId){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY+EndPoints.MS_CATEGORY+"/v0/get-categories-by-post/"+postId;
        try{
            return restTemplate.getForObject(resourceUrl, HashSet.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }

    }

    public List<CategoriesResponse> getAllCategoriesByPost(){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY+EndPoints.MS_CATEGORY+"/v0/find-all-categories-by-posts";
        try {
            ParameterizedTypeReference<List<CategoriesResponse>> type = new ParameterizedTypeReference<List<CategoriesResponse>>() {};
            ResponseEntity<List<CategoriesResponse>> listResponseEntity =
                    restTemplate.exchange(resourceUrl, HttpMethod.GET, null, type);
            return listResponseEntity.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
    }


    @CircuitBreaker(name="a-tentativi", fallbackMethod = "fallbackMethodATentativi")
    public ResponseEntity<?> getAllPublishedPosts() {

        String role = "ROLE_WRITER";
        List<UserResponse> writers = getUsersByRole(role);
        List<PostListResponseComplete> postResponses = postRepository.getAllPublishedPosts(LocalDateTime.now());
        List<CategoriesResponse> postsCategories = getAllCategoriesByPost();

        postResponses.forEach(p -> {
            writers.stream()
                    .filter(u -> u.getId() == p.getAuthorId())
                    .findFirst()
                    .ifPresent(u -> p.setAuthor(u.getUsername()));
            postsCategories.stream()
                    .filter(c -> c.getPostId() == p.getId())
                    .findFirst()
                    .ifPresent(c -> p.setCategories(c.getCategories()));
        });
/*
        for (int i = 0; i < writers.size(); i++) {
            long writerId = writers.get(i).getId();
            String author = writers.get(i).getUsername();

            for (int k = 0; k < postResponses.size(); k++) {
                if (writerId == postResponses.get(k).getAuthorId())
                    postResponses.get(k).setAuthor(author);

                for(int z = 0; z < postsCategories.size(); z++){
                    if(postResponses.get(k).getId() == postsCategories.get(z).getPostId())
                        postResponses.get(k).setCategories(postsCategories.get(z).getCategories());
                }
            }
        }

 */
        return new ResponseEntity(postResponses, HttpStatus.OK);
    }



    public List<UserResponse> getUsersByRole(String role){

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY + EndPoints.MS_AUTH + "/v0/get-users-by-role/" + role;

        try {
            ParameterizedTypeReference<List<UserResponse>> type = new ParameterizedTypeReference<List<UserResponse>>() {};
            ResponseEntity<List<UserResponse>> listResponseEntity =
                    restTemplate.exchange(resourceUrl, HttpMethod.GET, null, type);
            return listResponseEntity.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
    }

    public ResponseEntity<?> fallbackMethodATentativi(Exception e){
        return new ResponseEntity("SOMETHING WENT WRONG WITH AUTHORS", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<?> findPostsByCategory(String categoryName) {
       Set<Long> ids = getPostsByCategory(categoryName);
       List<PostListResponse> list = postRepository.getPostsByCategory(ids, LocalDateTime.now());
       return new ResponseEntity(list, HttpStatus.OK);
    }

    public Set<Long> getPostsByCategory(String categoryName){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY+EndPoints.MS_CATEGORY+"/v0/get-posts-by-category/"+categoryName;
        try{
            ParameterizedTypeReference<Set<Long>> type = new ParameterizedTypeReference<Set<Long>>() {};
            ResponseEntity<Set<Long>> listResponseEntity =
                    restTemplate.exchange(resourceUrl, HttpMethod.GET, null, type);
            return listResponseEntity.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }

    }

    public List<UserPostResponse> userPost() {
        return postRepository.userPost();
    }

    public List<AvgPosts> getAvgPosts(){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = EndPoints.GATEWAY+EndPoints.MS_RATING+"/v0/get-all-avg";
        try{
            ParameterizedTypeReference<List<AvgPosts>> type = new ParameterizedTypeReference<List<AvgPosts>>() {};
            ResponseEntity<List<AvgPosts>> listResponseEntity =
                    restTemplate.exchange(resourceUrl, HttpMethod.GET, null, type);
            return listResponseEntity.getBody();
        } catch (RestClientException e){
            log.error(e.getMessage());
            return null;
        }
    }

/*
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info("------- Proxy-Client-IP: "+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.info("------- WL-Proxy-Client-IP: "+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info("------- HTTP_CLIENT_IP: "+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info("------- HTTP_X_FORWARDED_FOR: "+ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            log.info("------- unknown: "+ip);
        }
        return ip;
    }
    */

}
