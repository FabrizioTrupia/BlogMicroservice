package it.cgmconsulting.msrating.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@AllArgsConstructor @Getter @Setter @NoArgsConstructor
public class RatingId implements Serializable {

    private long postId;

    private long userId;

    @Column(updatable = false, nullable = false)
    private LocalDate votedAt = LocalDate.now();

    public RatingId(long postId, long userId) {
        this.postId = postId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingId ratingId = (RatingId) o;

        if (postId != ratingId.postId) return false;
        if (userId != ratingId.userId) return false;
        return Objects.equals(votedAt, ratingId.votedAt);
    }

    @Override
    public int hashCode() {
        int result = (int) (postId ^ (postId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (votedAt != null ? votedAt.hashCode() : 0);
        return result;
    }
}
