package it.cgmconsulting.mscategory.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategoriesResponse {

    private long postId;
    Set<String> categories;

    public void addCategory(String cat){
        this.categories.add(cat);
    }

}
