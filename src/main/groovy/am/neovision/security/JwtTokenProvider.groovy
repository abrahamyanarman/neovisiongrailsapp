package am.neovision.security


import am.neovision.Role
import am.neovision.exception.CustomException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import java.util.stream.Collectors

@Component
 class JwtTokenProvider {

    @Value("\${serverPassword}")
    private String secretKey;

    @Value("\${jwtExpireLength}")
    private long validityInMilliseconds


    private MyUserDetailsService userDetailsService

    JwtTokenProvider(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes())
    }

    String createToken(String username, List<Role> roles, boolean remember) {

        Claims claims = Jwts.claims().setSubject(username)
        claims.put("auth", roles.stream().map({ s -> new SimpleGrantedAuthority(s.getAuthority()) }).filter(Objects.&nonNull).collect(Collectors.toList()))

        Date now = new Date()
        if (remember)
            validityInMilliseconds*=72
        Date validity = new Date(now.getTime() + validityInMilliseconds)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }


    Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities())
    }

    String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject()
    }

    String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
