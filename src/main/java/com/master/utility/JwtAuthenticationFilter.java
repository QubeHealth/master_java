package com.master.utility;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.api.ApiResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter implements Filter {
    private final String secretKey;

    public JwtAuthenticationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Extract user_id from JWT token
        String token = extractTokenFromHeader(request.getHeader("jwt-token"));
        if (token != null) {
            Long userId = extractUserIdFromToken(token);
            if (userId != null) {
                // Set user_id as a request attribute
                request.setAttribute("user_id", userId);
                filterChain.doFilter(request, response);
                return;
            }
        }

        // If the token is not valid or expired, send an unauthorized response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Construct JSON response with error message
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), new ApiResponse<>(false, "Invalid or Expired Token", null));
    }

    @Override
    public void destroy() {

    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null) {
            return authHeader;
        }
        return null;
    }

    private Long extractUserIdFromToken(String token) {
        try {
            byte[] secret = secretKey.getBytes();
            Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
            // Change casting to Long.valueOf() to handle Integer values as well
            return Long.valueOf(String.valueOf(claims.get("user_id")));
        } catch (Exception e) {
            // Log the exception for debugging purposes
            // Return appropriate error message
            return null;
        }
    }
}