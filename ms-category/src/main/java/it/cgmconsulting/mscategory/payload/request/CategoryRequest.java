package it.cgmconsulting.mscategory.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequest {

    @Min(1)
    private long categoryId;
    @NotBlank @Size(min=3, max=50)
    private String newCategoryName;
    private boolean newVisibility;
}
