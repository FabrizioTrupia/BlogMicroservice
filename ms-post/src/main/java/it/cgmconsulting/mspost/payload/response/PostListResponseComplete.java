package it.cgmconsulting.mspost.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
public class PostListResponseComplete extends PostListResponse {

    private Set<String> categories = new HashSet<>();
    private String author;
    private long authorId;

    public PostListResponseComplete(long id, String title, String overview, long authorId) {
        super(id, title, overview);
        this.authorId = authorId;
    }
}
