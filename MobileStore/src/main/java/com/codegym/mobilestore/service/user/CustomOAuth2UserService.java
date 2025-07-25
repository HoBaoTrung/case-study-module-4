package com.codegym.mobilestore.service.user;

import com.codegym.mobilestore.model.User;
import com.codegym.mobilestore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OidcUserService oidcUserService = new OidcUserService();
    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Autowired
    private IUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("CustomOAuth2UserService CALLED for provider: " + registrationId);

        OAuth2User oauth2User;

        // Google uses OpenID Connect (OIDC)
        if ("google".equals(registrationId)) {
            oauth2User = oidcUserService.loadUser((OidcUserRequest) userRequest);
        } else {
            oauth2User = defaultOAuth2UserService.loadUser(userRequest);
        }

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = null;
        String name = null;

        switch (registrationId) {
            case "google":
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
                break;
            case "github":
                email = (String) attributes.get("email");
                name = (String) attributes.get("login");
                break;
            case "facebook":
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
                break;
            default:
                throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        // Xử lý người dùng trong DB
        User user = userRepository.findByUsername(name);
        if (user == null) {
            User newUser = new User();
            newUser.setUsername(name);
            userRepository.save(newUser);
            System.out.println("✅ New user created: " + name);
        } else {
            System.out.println("ℹ️ Existing user: " + name);
        }

        return oauth2User;
    }
}

