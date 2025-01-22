package com.example.app.config;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.app.enums.RoleEnum;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.*;


@Component
public class JwtRequestFilter
        extends OncePerRequestFilter
{
    @Value( "${app.secret}" )
    String secret;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    public JwtRequestFilter()
    {
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
            throws ServerException, IOException, ServletException
    {

        if ("test".equals(activeProfile)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader( HttpHeaders.AUTHORIZATION );

        if ( HttpMethod.OPTIONS.name().equals( request.getMethod() ) )
        {
            response.setStatus( HttpServletResponse.SC_OK );
            filterChain.doFilter( request, response );
        }
        else
        {
            try
            {
                Optional<Cookie> optionalCookie =
                        request.getCookies() != null ? Arrays.stream( request.getCookies() ).filter(
                                cookie -> Objects.equals( cookie.getName(), "COOKIE" ) ).findFirst() : Optional.empty();

                String headerJwt = null;
                if ( authHeader != null && authHeader.startsWith( "Bearer " ) )
                {
                    headerJwt = authHeader.substring( 7 );
                }
                String token = optionalCookie.isPresent() ? optionalCookie.get().getValue() : headerJwt;

                if ( token != null )
                {
                    Jws<Claims> claims = Jwts.parser().setSigningKey( secret ).parseClaimsJws( token );
                    Claims claimsBody = claims.getBody();
                    String subject = claimsBody.getSubject();
                    List<String> roles  = claims.getBody().get( RoleEnum.USER.name() , ArrayList.class);

                    if (roles == null) {
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token roles");
                    } else {
                        SecurityContextHolder.getContext().setAuthentication( new TokenAuthentication( token, subject, roles));
                    }

                    request.setAttribute( "claims", claimsBody );
                    request.setAttribute( "jwtUserId", subject );
                    request.setAttribute("jwtUserRoles", roles);

                }
                filterChain.doFilter( request, response );
            }
            catch ( MalformedJwtException e )
            {
                response.sendError( HttpStatus.BAD_REQUEST.value(), "Missing or wrong token" );
            }
            catch ( ExpiredJwtException e )
            {
                response.sendError( HttpStatus.UNAUTHORIZED.value(), "Token expired or malformed" );
            }
        }
    }

}