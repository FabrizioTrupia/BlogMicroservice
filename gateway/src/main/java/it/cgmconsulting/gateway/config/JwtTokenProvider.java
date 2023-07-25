package it.cgmconsulting.gateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.cgmconsulting.gateway.model.User;
import it.cgmconsulting.gateway.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

    public static DecodedJWT verifyJwt(String jwt) throws TokenExpiredException {
        DecodedJWT decodedJwt = null;
        try {
            decodedJwt = JWT.require(Algorithm.HMAC512(Constants.JWT_SECRET)).build().verify(jwt);
            return decodedJwt;
        } catch (TokenExpiredException ex){
            return null;
        } catch (Exception e){
            log.error("++++++++++++++++ "+e.getMessage());
            return null;
        }
    }

    public User getUserJwt(String jwt) {
        DecodedJWT decoded =  verifyJwt(jwt);
        if(decoded == null)
            return null;
        return new User(
                Long.parseLong(decoded.getClaim("id").asString()),
                decoded.getClaim("sub").asString(),
                decoded.getClaim("roles").asString()
        );
    }
}
