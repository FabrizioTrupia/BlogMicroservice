package it.cgmconsulting.gateway.config;

import it.cgmconsulting.gateway.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouteValidator routeValidator;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();

        // verifico se l'endpoint richiede il token
        if(routeValidator.isOpenEndpoint(request))
            exchange.getResponse().setStatusCode(HttpStatus.OK);
        else {
            if(!isAuthMissing(request))
                return this.setCustomResponse(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
            String jwt = getJwtFromRequest(request);
            User user = new User();
            if(jwt == null)
                return this.setCustomResponse(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            else{
                user = jwtTokenProvider.getUserJwt(jwt);
                if(user == null)
                    return this.setCustomResponse(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                if(
                    (user.getAuthorities().contains("ROLE_ADMIN") && request.getURI().getPath().contains("v1")) ||
                    (user.getAuthorities().contains("ROLE_WRITER") && request.getURI().getPath().contains("v2")) ||
                    (user.getAuthorities().contains("ROLE_READER") && request.getURI().getPath().contains("v3"))
                )
                    populateRequestWithNewHeader(exchange, user);
                else
                    return this.setCustomResponse(exchange, "Invalid authorization", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private boolean isAuthMissing(ServerHttpRequest request){
        return request.getHeaders().containsKey("Authorization");
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getOrEmpty("Authorization").get(0);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void populateRequestWithNewHeader(ServerWebExchange exchange, User user){
        exchange.getRequest().mutate()
                .header("userId", String.valueOf(user.getId()))
                .header("username", user.getUsername())
                //.header("authorities", user.getAuthorities().toString())
                .build();
    }

    private Mono<Void> setCustomResponse(ServerWebExchange exchange, String errorMsg, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        DataBuffer buffer = response.bufferFactory().wrap(errorMsg.getBytes());
        return response.writeWith(Mono.just(buffer));
    }
}
