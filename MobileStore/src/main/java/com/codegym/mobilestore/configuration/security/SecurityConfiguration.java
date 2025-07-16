package com.codegym.mobilestore.configuration.security;

import com.codegym.mobilestore.service.user.UserService;
import com.codegym.mobilestore.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CustomSuccessHandle customSuccessHandle() {
        return new CustomSuccessHandle();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder(10));
    }

    protected void configure(HttpSecurity http) throws Exception {
        // Disable crsf cho đường dẫn /api/**
        //http.csrf().ignoringRequestMatchers("/**");
        http.httpBasic();
        http
                .formLogin(formLogin -> formLogin.successHandler(customSuccessHandle())
                )
                .formLogin(Customizer.withDefaults());

        http.authorizeHttpRequests(author -> author
                // ADMIN: ưu tiên trước
                .requestMatchers(HttpMethod.GET, "/products/add", "/products/*/edit").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/products/add", "/products/*/edit").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                // Các route yêu cầu login
                .requestMatchers("/checkout/**").authenticated()

                // Các route public
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/register", "/").permitAll()

                // Cuối cùng mới cho phép tất cả GET đến /products/**
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()

                // Static resources
                .requestMatchers("/css/**", "/js/**", "/images/**","/carts/**").permitAll()

                // Chặn tất cả còn lại
                .anyRequest().denyAll()
        ) .exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler()));
//        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/products/add","/products/*/edit")
        );

        //http.cors();
        http
                // ... other configurations
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirect after successful logout
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL))) // Clears site data including cookies
                );
    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}12345").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}12345").roles("ADMIN");
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/user**").hasRole("USER")
//                .requestMatchers("/admin**").hasRole("ADMIN")
//                .and()
//                .formLogin()
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
//    }
}
