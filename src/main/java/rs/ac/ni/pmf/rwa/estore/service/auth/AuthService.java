package rs.ac.ni.pmf.rwa.estore.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.ni.pmf.rwa.estore.model.dto.UserDto;
import rs.ac.ni.pmf.rwa.estore.model.dto.auth.AuthResponse;
import rs.ac.ni.pmf.rwa.estore.security.CustomUserDetails;
import rs.ac.ni.pmf.rwa.estore.security.JwtTokenUtil;
import rs.ac.ni.pmf.rwa.estore.security.RefreshTokenManager;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager _authenticationManager;
    private final JwtTokenUtil _jwtTokenUtil;
    private final RefreshTokenManager _refreshTokenManager;

    @Transactional(readOnly = true)
    public AuthResponse authenticate(final String username, final String password)
    {
        final Authentication authentication =
                _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authentication.getPrincipal() instanceof final CustomUserDetails userDetails)
        {
            final String accessToken = _jwtTokenUtil.generateToken(null, userDetails);
            final String refreshToken = _refreshTokenManager.createRefreshToken(username);
            final UserDto user = UserDto.builder()
                    .username(userDetails.getUsername())
                    .firstName(userDetails.getFirstName())
                    .lastName(userDetails.getLastName())
                    .locked(!userDetails.isAccountNonLocked())
                    .expired(!userDetails.isAccountNonExpired())
                    .credentialsExpired(!userDetails.isCredentialsNonExpired())
                    .enabled(userDetails.isEnabled())
                    .shouldChangePassword(userDetails.shouldChangePassword())
                    .roles(userDetails.getRoles())
                    .permissions(userDetails.getPermissions())
                    .build();

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(user)
                    .build();
        }

        throw new IllegalStateException("Invalid user authentication");
    }

    public void logout(final String username)
    {
        _refreshTokenManager.deleteRefreshToken(username);
    }
}
