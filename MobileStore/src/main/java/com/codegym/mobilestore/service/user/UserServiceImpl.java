package com.codegym.mobilestore.service.user;

import com.codegym.mobilestore.dto.UserDTO;
import com.codegym.mobilestore.model.User;
import com.codegym.mobilestore.model.UserPrinciple;
import com.codegym.mobilestore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserRepository.findByUsername(username);

        if (user != null) {
            return UserPrinciple.build(user);
        }
        return null;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRoles());
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            return getUserByUsername(userDetails.getUsername());
        }

        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return iUserRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return iUserRepository.save(user);
    }

    @Override
    public User delete(User user) {
        try {
            iUserRepository.delete(user);
            return user;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return iUserRepository.findAll();
    }
}
