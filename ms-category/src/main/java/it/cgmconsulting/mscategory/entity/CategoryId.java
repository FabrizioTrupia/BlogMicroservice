package it.cgmconsulting.mscategory.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CategoryId implements Serializable {

    @ManyToOne
    @JoinColumn(name="categoryId")
    Category category;

    private long postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
        return postId == that.postId && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, postId);
    }
}
