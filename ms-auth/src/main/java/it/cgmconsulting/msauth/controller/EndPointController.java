package it.cgmconsulting.msauth.controller;

import it.cgmconsulting.msauth.payload.response.UserResponse;
import it.cgmconsulting.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EndPointController {

    private final AuthService authService;

    @CrossOrigin("http://localhost:8090")
    @GetMapping("/v0/{userId}")
    public String getUsername(@PathVariable long userId){
        return authService.getUsername(userId);
    }

    @CrossOrigin("http://localhost:8090")
    @GetMapping("/v0/get-users-by-role/{role}")
    public List<UserResponse> getUsersByRole(@PathVariable String role){
        return authService.getUsersByRole(role);
    }
}
