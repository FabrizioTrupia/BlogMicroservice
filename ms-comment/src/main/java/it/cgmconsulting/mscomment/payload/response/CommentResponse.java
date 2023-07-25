package it.cgmconsulting.mscomment.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class CommentResponse {

    private long commentId;
    private String comment; // se censored = true il commento va oscurato con asterischi
    private LocalDateTime createdAt;
    private long authorId;
    private String author;

    public CommentResponse(long commentId, String comment, LocalDateTime createdAt, long authorId) {
        this.commentId = commentId;
        this.comment = comment;
        this.createdAt = createdAt;
        this.authorId = authorId;
    }
}
