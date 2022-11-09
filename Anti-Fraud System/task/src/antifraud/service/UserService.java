package antifraud.service;

import antifraud.dto.UserDTO;
import antifraud.model.User;

import java.util.List;

public interface UserService {

    List<UserDTO> getAll();

    User create(User user);

    void delete(String username);
}