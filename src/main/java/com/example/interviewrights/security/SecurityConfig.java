package com.example.interviewrights.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.interviewrights.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtRequestFilter jwtFilter) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth
						.requestMatchers(
							    "/api/auth/login",
							    "/api/auth/register",
							    "/api/auth/forgot-password",
							    "/api/auth/reset-password",
							    "/login.html",
							    "/register.html",
							    "/dashboard.html",
							    "/forgot-password.html",
							    "/reset-password.html",
							    "/admin/admin-dashboard.html",
							    "/admin/candidate.html",
							    "/admin/css/**",
							    "/css/**",
							    "/admin/js/**",
							    "/js/**"
							    ).permitAll()
						//.requestMatchers("/admin/admin-dashboard.html").hasAnyRole("ADMIN", "ADMIN_USER")
				       // .requestMatchers("/admin/**").hasAnyRole("ADMIN", "ADMIN_USER")
				       // .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "ADMIN_USER")
				      //  .requestMatchers("/dashboard.html").authenticated()
				        .requestMatchers("/api/**").authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}