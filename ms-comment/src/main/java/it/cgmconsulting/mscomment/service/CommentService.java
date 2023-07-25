package it.cgmconsulting.mscomment.service;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import it.cgmconsulting.mscomment.payload.response.UserResponse;
import it.cgmconsulting.mscomment.repository.CommentRepository;
import it.cgmconsulting.mscomment.utils.EndPoints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public ResponseEntity<?> createComment(CommentRequest request, long userId) {
        Comment c = new Comment(request.getComment(), userId, request.getPostId());
        commentRepository.save(c);
        return new ResponseEntity("Comment created", HttpStatus.CREATED);
    }



    public ResponseEntity<?> getComments(long postId) {

        String role = "ROLE_READER";
        List<UserResponse> readers = getUsersByRole(role);
        List<CommentResponse> comments = commentRepository.getComments(postId);

        comments.forEach(c -> {
            readers.stream()
                    .filter(u -> u.getId() == c.getAuthorId())
                    .findFirst()
                    .ifPresent(u -> c.setAuthor(u.getUsername()));
        });
        return new ResponseEntity(comments, HttpStatus.OK);
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
}
