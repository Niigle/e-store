package rs.ac.ni.pmf.rwa.estore.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.ni.pmf.rwa.estore.model.dto.auth.AuthRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.auth.AuthResponse;
import rs.ac.ni.pmf.rwa.estore.service.auth.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService _authService;

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody @Valid final AuthRequest authRequest)
    {
        return _authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(final Authentication authentication)
    {
        if (authentication != null)
        {
            final String username = authentication.getName();
            _authService.logout(username);
        }

        return ResponseEntity.ok().build();
    }

}
