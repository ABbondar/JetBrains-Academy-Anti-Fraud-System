package antifraud.service.impl;

import antifraud.dto.RoleDTO;
import antifraud.dto.AccessDTO;
import antifraud.dto.UserDTO;
import antifraud.exception.operation.OperationNotSupportException;
import antifraud.exception.role.RoleNotSupportException;
import antifraud.exception.user.UserNotFoundException;
import antifraud.model.Role;
import antifraud.model.User;
import antifraud.repository.UserRepository;
import antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public User create(User user) {
        if (userRepository.findAll()
                .stream()
                .anyMatch(u -> user.getUsername().equalsIgnoreCase(u.getUsername())))
            throw new ResponseStatusException(HttpStatus.CONFLICT);

        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encoder().encode(user.getPassword()));

        if (userRepository.findAll().isEmpty()) {

            user.setRole(Role.ADMINISTRATOR);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);

        } else {
            user.setRole(Role.MERCHANT);
        }
        return userRepository.save(user);
    }

    @Override
    public User update(RoleDTO role) {
        var user = userRepository.findByUsername(role.getUsername().toLowerCase())
                .orElseThrow(UserNotFoundException::new);

        if (user.getRole().name().equals(role.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);

        } else if (role.getRole().equalsIgnoreCase(Role.MERCHANT.toString())
                || role.getRole().equalsIgnoreCase(Role.SUPPORT.toString())) {

            user.setRole(Role.valueOf(role.getRole().toUpperCase()));
            return userRepository.save(user);
        }
        throw new RoleNotSupportException();
    }

    @Override
    public User access(AccessDTO operation) {
        var user = userRepository.findByUsername(operation.getUsername().toLowerCase())
                .orElseThrow(UserNotFoundException::new);

        if (operation.getOperation().equalsIgnoreCase("unlock")) {
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            return userRepository.save(user);

        } else if (operation.getOperation().equalsIgnoreCase("lock")) {
            user.setAccountNonExpired(false);
            user.setAccountNonLocked(false);
            user.setCredentialsNonExpired(false);
            user.setEnabled(false);
            return userRepository.save(user);
        }
        throw new OperationNotSupportException();
    }

    @Override
    @Transactional
    public void delete(String username) {
        var user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteUserByUsername(user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(UserNotFoundException::new);
    }

    protected PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }
}