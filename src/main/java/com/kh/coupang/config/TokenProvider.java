package com.kh.coupang.config;

import com.kh.coupang.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class TokenProvider {

    private SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String create(User user){
        // 얘는 builder.build 가 아닌 compact로 끝남
        return Jwts.builder()
                .signWith(secretKey)
                //.setSubject(user.getId()) 이거는 하나 값만 보낼때
                // 여러 값을 토큰에 넣을때는 Claims 형태는 Map
                .setClaims(Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                ))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .compact();
    }

    public User validateGetUser(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();

        return User.builder()
                // 토큰 만들때 설정한 Map 의 id 쪽이 들어옴 즉  name(혹은 key) 값이 들어온다고 보면됨
                .id((String) claims.get("id"))
                .name((String)claims.get("name"))
                .email((String)claims.get("email"))
                .role((String) claims.get("role"))
                .build();
    }
}
