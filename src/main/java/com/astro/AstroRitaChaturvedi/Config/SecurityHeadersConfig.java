package com.astro.AstroRitaChaturvedi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityHeadersConfig {

    @Bean
    public OncePerRequestFilter securityHeadersFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                          FilterChain filterChain) throws ServletException, IOException {
                
                // Add security headers
                response.setHeader("X-Content-Type-Options", "nosniff");
                response.setHeader("X-Frame-Options", "SAMEORIGIN");
                response.setHeader("X-XSS-Protection", "1; mode=block");
                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                
                // CSP for Angular + Razorpay + Phone.email
                String csp = "default-src 'self'; " +
                           "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://checkout.razorpay.com https://www.phone.email; " +
                           "style-src 'self' 'unsafe-inline'; " +
                           "img-src 'self' data: https:; " +
                           "font-src 'self' data:; " +
                           "connect-src 'self' https://api.razorpay.com https://www.phone.email https://user.phone.email; " +
                           "frame-src 'self' https://api.razorpay.com https://www.phone.email";
                response.setHeader("Content-Security-Policy", csp);
                
                // Remove server information
                response.setHeader("Server", "");
                
                filterChain.doFilter(request, response);
            }
        };
    }
}