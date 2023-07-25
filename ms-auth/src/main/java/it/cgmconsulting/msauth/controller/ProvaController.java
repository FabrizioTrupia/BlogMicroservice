package it.cgmconsulting.msauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProvaController {

    @GetMapping // localhost:{port}/ms-auth
    public String prova(){
        return "Ciao pubblico";
    }

    @GetMapping("/v1") // localhost:{port}/ms-auth/v1
    public String prova1(){
        return "Ciao amministratori!";
    }

    @GetMapping("/v2") // localhost:{port}/ms-auth/v2
    public String prova2(@RequestHeader("username") String username){
        return "Ciao scrittore "+username;
    }

    @GetMapping("/v3") // localhost:{port}/ms-auth/v3
    public String prova3(){
        return "Ciao lettori!";
    }


}
