package com.vk.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vk.filter.AuthTokenFilter;
import com.vk.service.LibraryService;

import lombok.SneakyThrows;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

	@Autowired
	@Lazy
	private LibraryService libService;
	
	@Bean
	public BCryptPasswordEncoder   pwdEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	    public AuthTokenFilter authenticationJwtTokenFilter() {
	        return new AuthTokenFilter();
	    }
	
	 //it fetch the record from the table based on username (it calls loadUserByUsername(String uName automatically) )
	@Bean
	public DaoAuthenticationProvider  daoAuthProvider() {
		DaoAuthenticationProvider daoAuthP =
				   new DaoAuthenticationProvider();
		
		 daoAuthP.setPasswordEncoder(pwdEncoder());//here PasswordEncoder object is used by AuthManager to encrypt login password
		 daoAuthP.setUserDetailsService(libService);
		 
		 return daoAuthP ;
	}
	
	@Bean
	@SneakyThrows
	public AuthenticationManager authManager(AuthenticationConfiguration authConfig) {
		return authConfig.getAuthenticationManager();
	}
	
	/* .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll() // Allow login/register without token
                .requestMatchers("/books/**", "/my-books/**").hasRole("STUDENT")
                .requestMatchers("/issues/**").hasRole("LIBRARIAN")
                .requestMatchers("/librarian/**").hasRole("LIBRARIAN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT is stateless
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();*/
	
	@Bean
	@SneakyThrows
	public SecurityFilterChain sFilterChain(HttpSecurity http) {
		
		http.authorizeHttpRequests(req->{
			
			req.requestMatchers(
		            "/swagger-ui/**",
		            "/swagger-ui.html",
		            "/v3/api-docs/**",
		            "/swagger-resources/**",
		            "/webjars/**"
		        ).permitAll()
			
			.requestMatchers("/registerUser","/login")
			.permitAll()
			.requestMatchers("/submitBookAndCalculateChagres/**","/addBookInLibrary","/saveBookIssuedDetails","/addMoreBooksOnExisting/**").hasRole("LIBRARIAN")
			.requestMatchers("/courseValidity","/searchBook","/showAllBook").hasAnyRole("STUDENT","LIBRARIAN")
			.anyRequest()
			.authenticated();
		});
		http.csrf().disable();
		
		/* Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
		//UsernamePasswordAuthenticationFilter.class → this is the default Spring Security filter 
		that handles form login (username + password).*/
		
		/*addFilterBefore() → tells Spring Security:
         "Hey Spring, please run my AuthTokenFilter before the default UsernamePasswordAuthenticationFilter."*/
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
	}
}
