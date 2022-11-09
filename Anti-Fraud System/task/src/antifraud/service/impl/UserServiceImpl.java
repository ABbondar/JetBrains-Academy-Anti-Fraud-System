package antifraud.service.impl;

import antifraud.dto.UserDTO;
import antifraud.exception.UserExistsException;
import antifraud.exception.UserNotFoundException;
import antifraud.model.Role;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(UserDTO::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        if (userRepository
                .findAll()
                .stream()
                .anyMatch(u -> user.getUsername().equalsIgnoreCase(u.getUsername())))
            throw new UserExistsException();

        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encoder().encode(user.getPassword()));
        user.grantAuthority(Role.USER);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(String username) {
        var user = userRepository
                .findByUsername(username.toLowerCase())
                .orElseThrow(UserNotFoundException::new);

        userRepository.deleteUserByUsername(user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username.toLowerCase())
                .orElseThrow(UserNotFoundException::new);
    }

    protected PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }
}