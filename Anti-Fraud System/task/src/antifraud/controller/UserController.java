package antifraud.controller;

import antifraud.dto.UserDTO;
import antifraud.model.User;
import antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers() {
        log.info("[GET] Request to read all users");

        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Validated @RequestBody User user) {
        log.info("[POST] Request to create user");

        var newUser = userService.create(user);
        return new ResponseEntity<>(UserDTO.mapToUserDTO(newUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUserByUserName(@PathVariable String username) {
        log.info("[DELETE] Request to delete user by username");

        userService.delete(username);
        return ResponseEntity.ok(Map.of(
                "username", username,
                "status", "Deleted successfully!"));
    }
}