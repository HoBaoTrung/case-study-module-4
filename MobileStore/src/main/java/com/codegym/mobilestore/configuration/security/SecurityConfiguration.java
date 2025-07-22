package com.codegym.mobilestore.configuration.security;

import com.codegym.mobilestore.service.user.UserService;
import com.codegym.mobilestore.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@PropertySource("classpath:secret.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
//
//    @Value("${google.client-id}")
//    private String clientId;
//    @PostConstruct
//    public void testProps() {
//        System.out.println("📦 clientId from @Value = " + clientId);
//    }
//    @Value("${google.client-secret}")
//    private String clientSecret;
//
//    @Value("${google.redirect-uri}")
//    private String redirectUri;

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
//                .requestMatchers(HttpMethod.POST, "/register").permitAll()
//                .requestMatchers(HttpMethod.GET, "/register", "/").permitAll()
//
//                // Cuối cùng mới cho phép tất cả GET đến /products/**
//                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
//
//                // Static resources
//                .requestMatchers("/css/**", "/js/**", "/images/**","/carts/**").permitAll()

                        // Chặn tất cả còn lại
//                .anyRequest().denyAll()
                        .anyRequest().permitAll()
        ).exceptionHandling(customizer -> customizer.accessDeniedHandler(customAccessDeniedHandler()));
//        http.csrf(AbstractHttpConfigurer::disable);
        http.csrf(csrf -> csrf
                // Disable crsf cho vài đường dẫn /api/**
                .ignoringRequestMatchers("/products/add", "/products/*/edit")
        ).oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/welcome")
        );

        //http.cors();
        http
                // ... other configurations
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirect after successful logout
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL))) // Clears site data including cookies
                );


    }

    @Bean
    public ClientRegistration googleClientRegistration(
            @Value("${google.client-id}") String clientId,
            @Value("${google.client-secret}") String clientSecret,
            @Value("${google.redirect-uri}") String redirectUri
    ) {

        return ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                .userNameAttributeName("sub")
                .clientName("Google")
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ClientRegistration googleClientRegistration) {
        return new InMemoryClientRegistrationRepository(googleClientRegistration);
    }


}
