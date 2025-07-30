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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = iUserRepository.findByUsername(input);

        if (user == null) {
            user = iUserRepository.findByEmail(input);
        }

        if (user != null) {
            return UserPrinciple.build(user);
        }
        return null;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRoles(),user.getEmail());
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
    public User create(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return iUserRepository.save(user);
    }

    @Override
    public User update(Long id, UserDTO dto) {
        User user = iUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return iUserRepository.save(user);
    }

    @Override
    public UserDTO getById(Long id) {
        User user = iUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
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
