package com.crazzy.rahul.ppmtool.security;

import com.crazzy.rahul.ppmtool.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.crazzy.rahul.ppmtool.security.SecurityConstants.SECRET;
import static com.crazzy.rahul.ppmtool.security.SecurityConstants.TOKEN_EXPIRATION_TIME;

@Component
@Slf4j
public class JwtTokenProvider {

    // Generate Token
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expireDate = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

        String userId = Long.toString(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    // Validate Token
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException ex) {
            log.info("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            log.info("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException ex) {
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException ex) {
            log.info("JWT Claims string is Empty");
        }

        return false;
    }

    // Get UserID from Token
    public Long getUserIdFromJwt(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwtToken).getBody();
        String id = (String) claims.get("id");

        return Long.valueOf(id);
    }
}
