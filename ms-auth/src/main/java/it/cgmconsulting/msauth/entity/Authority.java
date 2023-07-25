package it.cgmconsulting.msauth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private long id;

    @Column(length = 20, nullable = false, unique = true)
    private String authorityName; // authority_name

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return id == authority.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
