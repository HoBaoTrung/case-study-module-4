package com.codegym.mobilestore.service.user;

import com.codegym.mobilestore.model.User;
import com.codegym.mobilestore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BaseOAuthUserService {

    @Autowired
    protected IUserRepository userRepository;

    protected User handleUser(String provider, Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            if (!existingUser.getProvider().equalsIgnoreCase(provider)) {
                throw new AuthenticationServiceException(
                        "Email " + email + " đã được đăng ký bằng tài khoản " + existingUser.getProvider()
                );
            }
            return existingUser;
        }

        existingUser = userRepository.findByUsername(name);
        if (existingUser != null) {
            if (!existingUser.getProvider().equalsIgnoreCase(provider)) {
                throw new AuthenticationServiceException(
                        "Username " + name + " đã được đăng ký bằng tài khoản " + existingUser.getProvider()
                );
            }
            return existingUser;
        }

        User newUser = new User();
        newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(generateUniqueUsername(name));
        newUser.setProvider(provider);
        userRepository.save(newUser);

        return newUser;
    }

    private String generateUniqueUsername(String baseName) {
        String username = baseName.toLowerCase().replaceAll("\\s+", "");
        int suffix = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseName + suffix++;
        }
        return username;
    }
}
