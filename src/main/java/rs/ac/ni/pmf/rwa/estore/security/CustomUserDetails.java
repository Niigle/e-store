package rs.ac.ni.pmf.rwa.estore.security;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final Set<String> roles;
    private final Set<String> permissions;

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        final Set<GrantedAuthority> authorities = new HashSet<>();

        roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);

        return authorities;
    }

    @Override
    public @Nullable String getPassword()
    {
        return password;
    }

    @Override
    @NullMarked
    public String getUsername()
    {
        return username;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
//		return !_userEntity.isExpired();
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
//		return !_userEntity.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
//		return !_userEntity.isCredentialsExpired();
    }

    @Override
    public boolean isEnabled()
    {
        return true;
//		return _userEntity.isEnabled();
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public Long getId()
    {
        return id;
    }

    public boolean shouldChangePassword()
    {
        return false;
//		return _userEntity.isShouldChangePassword();
    }

}
