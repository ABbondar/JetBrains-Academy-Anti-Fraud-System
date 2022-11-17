package antifraud.service;

import antifraud.dto.RoleDTO;
import antifraud.dto.AccessDTO;
import antifraud.dto.UserDTO;
import antifraud.model.User;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll();

    User create(User user);

    User update(RoleDTO role);

    User access(AccessDTO operation);

    void delete(String username);
}