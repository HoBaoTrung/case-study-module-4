package com.codegym.mobilestore.component;

import com.codegym.mobilestore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserProcessor {
    @Autowired
    private IUserRepository userRepository;

}
