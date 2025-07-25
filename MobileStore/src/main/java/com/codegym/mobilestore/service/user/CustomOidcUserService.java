package com.codegym.mobilestore.service.user;

import com.codegym.mobilestore.model.User;
import com.codegym.mobilestore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOidcUserService CALLED");

        // Gọi super để load user từ ID token
        OidcUser oidcUser = super.loadUser(userRequest);

        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // Xử lý người dùng
        User user = userRepository.findByUsername(name);
        if (user == null) {
            User newUser = new User();
            newUser.setUsername(name);
            userRepository.save(newUser);
        }

        return oidcUser;
    }
}
