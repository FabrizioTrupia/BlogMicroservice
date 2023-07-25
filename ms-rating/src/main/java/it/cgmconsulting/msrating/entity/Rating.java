package it.cgmconsulting.msrating.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
public class Rating {

    // un utente può votare un post una volta sola al giorno
    // quindi in un anno un posto può ottenre al max 365 voti per utente
    // PK : postId + userId + LocalDate

    @EmbeddedId
    @EqualsAndHashCode.Include
    private RatingId ratingId;

    @EqualsAndHashCode.Exclude
    private byte rate;


}
