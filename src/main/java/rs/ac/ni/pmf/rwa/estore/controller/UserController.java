package rs.ac.ni.pmf.rwa.estore.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.model.dto.UserDto;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.UserRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.UpdatePasswordRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.UserResponse;
import rs.ac.ni.pmf.rwa.estore.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
                /*.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());*/
    }

    @GetMapping("/{id}")
    public UserResponse  getUserById(@PathVariable final Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody @Valid final UserDto userDto) {

        return userService.updateUser(id, userDto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        try {
            userService.changePassword(id, updatePasswordRequest);
            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
    }
}