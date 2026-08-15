package rs.ac.ni.pmf.rwa.estore.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Collections;
import java.util.Set;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDto {

    Long id;

    @NotBlank(message = "Username cannot be blank")
    String username;
    String firstName;
    String lastName;
    String address;
    String phone;
    @Email(message = "Email is not valid")
    String email;

    @Pattern(regexp = "^$|.*\\S.*", message = "Must not be blank if provided")
    String phoneNumber;

    @Builder.Default
    boolean enabled = true;
    @Builder.Default
    boolean expired = false;

    @Builder.Default
    boolean locked = false;

    @Builder.Default
    boolean credentialsExpired = false;

    @Builder.Default
    boolean shouldChangePassword = false;

    @Builder.Default
    Set<String> roles = Collections.emptySet();

    @Builder.Default
    Set<String> permissions = Collections.emptySet();

}
