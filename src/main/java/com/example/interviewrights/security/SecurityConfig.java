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
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/forgot-password",
						"/api/auth/reset-password", "/api/user/invite/register/**", "/api/auth/invite",
						"/api/user/validate", "/api/interview/validate", "/api/interview/verify", "/api/trust/init",
						"/api/trust/flag", "/api/trust/heartbeat", "/api/interview/complete", "/api/trust/terminate",
						"/api/user/**", "/login.html", "/interview.html", "/register.html", "/dashboard.html",
						"/forgot-password.html", "/reset-password.html", "/admin/admin-dashboard.html",
						"/admin/candidate.html", "/admin/edit-candidate.html", "/admin/invite-user.html",
						"/admin/register-invite.html", "/myProfile.html", "/admin/css/**", "/css/**", "/js/**",
						"/admin/js/**", "/models/**", "interview-room.html", "interview-terminated.html",
						"/topic/offer", "/topic/answer", "/topic/ice", "/signal", "/api/trust/results",
						"interview-results.html", "/api/trust/finish", "/api/face/verify","/admin/findCandidates.html","/api/admin/candidates/allPublicCandidates")
				.permitAll().requestMatchers("/signal/**").permitAll()

				.requestMatchers("/topic/**").permitAll()

				.requestMatchers("/app/**").permitAll()

				.requestMatchers("/api/**").authenticated()

				.anyRequest().permitAll())

				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}