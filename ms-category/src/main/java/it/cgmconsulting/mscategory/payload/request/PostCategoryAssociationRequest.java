package it.cgmconsulting.mscategory.payload.request;

import lombok.Getter;

import java.util.Set;
@Getter
public class PostCategoryAssociationRequest {
    private long postId;
    private Set<Long> categoryIds;
}
