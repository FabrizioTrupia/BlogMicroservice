package it.cgmconsulting.msrating.controller;

import it.cgmconsulting.msrating.payload.response.AvgPosts;
import it.cgmconsulting.msrating.service.RatingService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8090")
public class EndPointController {

    private final RatingService ratingService;

    @GetMapping("/v0/get-avg-by-post/{postId}")
    public double getAvgByPost(@PathVariable @Min(1) long postId){
        return ratingService.getAvgByPost(postId);
    }

    @GetMapping("/v0/get-all-avg")
    public List<AvgPosts> getAllAvg(){
        return ratingService.getAvgPosts();
    }
}
