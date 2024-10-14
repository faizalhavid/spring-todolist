package faizal.project.todo_list.helpers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;

import faizal.project.todo_list.exception.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTHelper {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final long EXPIRATION_TIME = 1200; // in minutes



    public static String generateToken(String email, Long userId) {
        var now = Instant.now();
        System.out.println("User ID:1" + userId);
        return Jwts.builder().claims().subject(email).add("user_id", userId).and().expiration(Date.from(now.plus(EXPIRATION_TIME, ChronoUnit.MINUTES)))
                .signWith(SECRET_KEY).compact();
    }


    public static String extractUsername(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public static Long extractUserId(String token) {
        System.out.println("tess");
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().get("user_id", Long.class);
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    private static boolean isTokenExpired(String token) {
        return  Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
}
