package it.cgmconsulting.mspost.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
public class PostResponse {

    private long id;
    private String title;
    private String content;
    private LocalDateTime publishedAt;
    private long authorId;
    private String author;
    private Set<String> categories;
    private double avgRating;

    public PostResponse(long id, String title, String content, LocalDateTime publishedAt, long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.publishedAt = publishedAt;
        this.authorId = authorId;
    }
}
