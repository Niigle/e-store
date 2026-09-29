package rs.ac.ni.pmf.rwa.estore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
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
    public Page<UserResponse> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        return userService.getAllUsers(pageable);
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
    public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody @Valid final UserDto userDto) {

        return userService.updateUser(id, userDto);
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update password", description = "Currently logged user password change.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "password updated successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or wrong old password",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthenticated user (missing or invalid JWT token)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with requested ID is not found",
                    content = @Content
            )
    })
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        try {
            userService.changePassword(id, updatePasswordRequest);
            return ResponseEntity.ok("Password updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);
    }
}