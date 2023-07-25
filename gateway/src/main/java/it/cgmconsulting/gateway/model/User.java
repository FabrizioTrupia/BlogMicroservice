package it.cgmconsulting.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @NoArgsConstructor @AllArgsConstructor @Getter
public class User {

    private long id;
    private String username;
    private String authorities;

}
