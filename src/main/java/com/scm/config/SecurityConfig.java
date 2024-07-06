package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails user1 = User.withDefaultPasswordEncoder()
    // .username("admin123")
    // .password("admin123")
    // // .roles("ADMIN", "USER")
    // .build();
    // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);

    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandle oAuthAuthenticationSuccessHandle;

    // configuration of authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // configuration

        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/register").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // Form default login
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/dashboard");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
        });

        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(oAuthAuthenticationSuccessHandle);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
