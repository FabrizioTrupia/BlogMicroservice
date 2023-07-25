package it.cgmconsulting.mscategory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private long id;

    @Column(length = 50, nullable = false, unique = true)
    private String categoryName;

    private boolean visible = true;



    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }
}

