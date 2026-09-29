package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Value
@Getter
@Setter
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name is mandatory.")
    private String firstName;

    @NotBlank(message = "Lastname is mandatory")
    private String lastName;

    @NotBlank(message = "username is mandatory")
    @Size(min = 2, max = 50, message = "username must be between 2 and 50 characters.")
    private String username;

    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "address is mandatory.")
    private String address;

    @NotBlank(message = "phone is mandatory.")
    private String phone;
    private String password;

}
