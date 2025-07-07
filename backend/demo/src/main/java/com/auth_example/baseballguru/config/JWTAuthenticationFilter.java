package com.auth_example.baseballguru.config;

import com.auth_example.baseballguru.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTAuthenticationFilter(
            JWTService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    )
    {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException
    {
        String jwt = null;
        String authHeader = request.getHeader("Authorization");

        // FOR POSTMAN
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            // FOR HTML ONLY COOKIES
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if ("jwt".equals(c.getName())) {
                        jwt = c.getValue();
                        break;
                    }
                }
            }
        }

        // continues chain if no token
        if (jwt == null || jwt.trim().isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println(userDetails);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            System.out.println("JWT Success - pt 1");
        } catch (Exception ex) {
            System.out.println("JWT Authentication Failed: " + ex.getMessage());
            //handlerExceptionResolver.resolveException(request, response, null, ex);
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

}
