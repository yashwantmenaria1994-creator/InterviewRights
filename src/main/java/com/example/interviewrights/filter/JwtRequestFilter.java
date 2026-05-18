package com.example.interviewrights.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.interviewrights.entity.User;
import com.example.interviewrights.entity.UserSession;
import com.example.interviewrights.repository.SessionRepository;
import com.example.interviewrights.security.JwtUtil;
import com.example.interviewrights.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private SessionRepository sessionRepository;
	
	private final JwtUtil jwtUtil;
    private final AuthService authService;

    public JwtRequestFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String path = request.getServletPath();
        if (path.equals("/api/auth/login") || path.equals("/api/auth/register") || path.startsWith("/api/auth/")
        		|| path.equals("/api/auth/forgot-password") || path.equals("/api/auth/reset-password")|| path.startsWith("/signal")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);
            String userEmail = jwtUtil.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = authService.findByEmail(userEmail);

                // 🔥 SESSION VALIDATION (IMPORTANT)
                UserSession session = sessionRepository.findByUserId(user.getId()).orElse(null);

                if (session == null || !session.getToken().equals(jwt)) {
                    // ❌ Invalid / Old Session → force logout
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // ✅ VALID SESSION → allow
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                null,
                                List.of(new SimpleGrantedAuthority(user.getRole()))
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}