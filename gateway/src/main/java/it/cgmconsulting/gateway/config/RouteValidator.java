package it.cgmconsulting.gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {

    // v0 oppure actuator indicano che non necessita il token per la chiamata

    public boolean isOpenEndpoint(ServerHttpRequest request){
        if(request.getURI().getPath().contains("v0") || request.getURI().getPath().contains("actuator"))
            return true;
        return false;
    }
}
