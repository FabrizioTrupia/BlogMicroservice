package it.cgmconsulting.msrating.controller;

import it.cgmconsulting.msrating.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class RatingController { // è possibile solo inserire un voto al giorno; l'aggiornamento del voto non è previsto.

    private final RatingService ratingService;

    @PostMapping("/v3/{postId}/{rate}")
    public ResponseEntity<?> addRate(
            @PathVariable @Min(1) long postId,
            @PathVariable @Min(1) @Max(5) byte rate,
            @RequestHeader("userId") long userId){
        return ratingService.addRate(postId, userId, rate);
    }
}
