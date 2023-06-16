package com.Devas.BackendPrescription.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static String secretKey;
    private static int expiration;

    @Value("${devas.jwt.secret}")
    public void setSecret(String secret){
        this.secretKey=secret;
    }

    @Value("${devas.jwt.expiration}")
    public void setExpiration(int expiration){
        this.expiration = expiration;
    }

    public String GenerateJwtToken(String username){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte [] ApiKeySecret = DatatypeConverter.parseBase64Binary(secretKey);
        Key SignKey = new SecretKeySpec(ApiKeySecret,signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(new Date().getTime()+expiration))
                .setIssuedAt(new Date())
                .setIssuer("devas")
                .signWith(signatureAlgorithm,SignKey);
        return builder.compact();
    }

    public Boolean validateJwt(String validate){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(validate);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Claims DecodeJwtToken(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(jwt).getBody();
   //     System.out.println(claims.getSubject());
        return claims;
    }
}
