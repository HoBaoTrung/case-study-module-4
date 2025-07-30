package com.codegym.mobilestore.service.user;

import com.codegym.mobilestore.dto.UserDTO;
import com.codegym.mobilestore.model.User;
import com.codegym.mobilestore.service.IGeneralService;

public interface UserService extends IGeneralService<User> {
    User getCurrentUser();

    User getUserByUsername(String username);

    User create(UserDTO dto);

    User update(Long id, UserDTO dto);

    UserDTO getById(Long id);
}
