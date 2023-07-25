package it.cgmconsulting.mspost.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank @Size(max=100, min=2)
    private String title;
    @NotBlank @Size(max=255, min=10)
    private String overview;
    @NotBlank @Size(max=20000, min=100)
    private String content;

}
