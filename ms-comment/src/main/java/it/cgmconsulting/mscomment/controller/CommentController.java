package it.cgmconsulting.mscomment.controller;

import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v3")
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequest request, @RequestHeader("userId") long userId){
        return commentService.createComment(request, userId);
    }

    @GetMapping("/v0/{postId}")
    public ResponseEntity<?> getComments(@PathVariable long postId){
        return commentService.getComments(postId);
    }


}
