package it.cgmconsulting.msauth.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.Set;

@Getter
public class ChangeRoleRequest {

    @Min(1)
    private long id;

    @NotEmpty
    private Set<String> newAuthorities;
}
