package com.study.web.auth;

import com.study.web.auth.handler.AccessDeniedExceptionHandler;
import com.study.web.auth.handler.AuthenticationEntryPoint;
import com.study.web.auth.handler.FilterExceptionHandler;
import com.study.web.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final RedisService redisService;
	private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;
	private final TokenProvider tokenProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				.formLogin(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request
						.anyRequest().permitAll()
				)
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.authenticationEntryPoint(authenticationEntryPoint)
						.accessDeniedHandler(accessDeniedExceptionHandler)
				)
				.with(new SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
					@Override
					public void configure(HttpSecurity http) {
					}
				}, customizer -> customizer.configure(
						http
								.addFilterBefore(
										new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class
								)
								.addFilterBefore(
										new FilterExceptionHandler(), UsernamePasswordAuthenticationFilter.class
								)
				));

		return http.build();
	}
}
