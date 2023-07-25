package it.cgmconsulting.msauth.controller;

import it.cgmconsulting.msauth.payload.request.ChangeRoleRequest;
import it.cgmconsulting.msauth.payload.request.SignInRequest;
import it.cgmconsulting.msauth.payload.request.SignUpRequest;
import it.cgmconsulting.msauth.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v0/signup") // localhost:8090/ms-auth/signup
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/v0/signin") // localhost:8090/ms-auth/signin
    public ResponseEntity<?> signin(@RequestBody @Valid SignInRequest request) {
        return authService.signin(request);
    }

    @PutMapping("/v1/change-roles")
    public ResponseEntity<?> changeRoles(
            @RequestBody @Valid ChangeRoleRequest request, @RequestHeader("userId") long headerId){
        return authService.changeRoles(request, headerId);
    }



}
