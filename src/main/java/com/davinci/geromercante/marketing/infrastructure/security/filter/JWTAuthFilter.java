package com.davinci.geromercante.marketing.infrastructure.security.filter;

import com.davinci.geromercante.marketing.common.util.JwtUtil;
import com.davinci.geromercante.marketing.infrastructure.security.CustomAuthenticationEntryPoint;
import com.davinci.geromercante.marketing.module.auth.service.SessionService;
import com.davinci.geromercante.marketing.module.auth.service.impl.CredentialServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CredentialServiceImpl credentialService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final SessionService sessionService;

    public JWTAuthFilter(JwtUtil jwtUtil, CredentialServiceImpl credentialService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, SessionService sessionService) {
        this.jwtUtil = jwtUtil;
        this.credentialService = credentialService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String userEmail;

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

          try {
              if (jwtUtil.getPasswordRecoveryId(authHeader) == null) {
                  if (sessionService.isTokenRevoked(jwtUtil.getToken(authHeader))) {
                      AuthenticationException authException = new AuthenticationServiceException("Token revoked");
                      customAuthenticationEntryPoint.commence(request, response, authException);
                      return;
                  }
              }
              userEmail = jwtUtil.extractUsername(authHeader);
              if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                  UserDetails userDetails = credentialService.loadUserByUsername(userEmail);
                  if (jwtUtil.isTokenValid(authHeader, userDetails)) {
                      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                      SecurityContextHolder.getContext().setAuthentication(authToken);
                  }
              }
          } catch (ExpiredJwtException e) {
              AuthenticationException authException = new AuthenticationServiceException("Expired JWT token", e);
              customAuthenticationEntryPoint.commence(request, response, authException);
              return;
          }

        filterChain.doFilter(request, response);
    }
}
