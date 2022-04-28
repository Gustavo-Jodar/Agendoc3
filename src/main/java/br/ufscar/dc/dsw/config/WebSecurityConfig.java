package br.ufscar.dc.dsw.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.security.UsuarioDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UsuarioDetailsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()

				.authorizeRequests()
				.antMatchers("/", "/index", "/error", "/users/**").permitAll()
				.antMatchers("/static/**", "/login/**", "/scripts/**", "/styles/**", "/images/**", "/webjars/**")
				.permitAll()
				.antMatchers("/admins/**").permitAll()
				.antMatchers("/clientes/{\\d+}**").permitAll()
				.antMatchers("/profissionais/{\\d+}**").permitAll()
				.antMatchers("/aux/**").hasAnyRole("ADMIN", "CLIENTE", "PROFISSIONAL")
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.logoutSuccessUrl("/")
				.permitAll();
	}

}
