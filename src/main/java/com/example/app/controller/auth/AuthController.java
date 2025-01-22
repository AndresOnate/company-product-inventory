package com.example.app.controller.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import com.example.app.enums.RoleEnum;
import com.example.app.exception.InvalidCredentialsException;
import com.example.app.model.User;
import com.example.app.service.UserService;

import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;


@RestController
@RequestMapping( "/api/auth" )
@CrossOrigin(origins = "*")
public class AuthController
{

    @Value( "${app.secret}" )
    String secret;

    private final UserService userService;

    public AuthController( @Autowired UserService userService )
    {
        this.userService = userService;
    }

    @PostMapping
    public TokenDto login( @RequestBody LoginDto loginDto )
    {
        User user = userService.findByEmail( loginDto.email );
        if ( BCrypt.checkpw( loginDto.password, user.getPassword() ) )
        {
            return generateTokenDto( user );
        }
        else
        {
            throw new InvalidCredentialsException();
        }

    }

    private String generateToken( User user, Date expirationDate )
    {
        return Jwts.builder()
                .setSubject( user.getEmail() )
                .claim( RoleEnum.USER.name(), user.getRoles() )
                .setIssuedAt(new Date() )
                .setExpiration( expirationDate )
                .signWith( SignatureAlgorithm.HS256, secret )
                .compact();
    }

    private TokenDto generateTokenDto( User user )
    {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add( Calendar.MINUTE, 10 );
        String token = generateToken( user, expirationDate.getTime() );
        return new TokenDto( token, expirationDate.getTime() );
    }
}