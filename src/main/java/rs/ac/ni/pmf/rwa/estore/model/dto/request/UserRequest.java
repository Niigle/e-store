package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request payload for creating or updating a user profile")
public class UserRequest {

    @NotBlank(message = "Name is mandatory.")
    @Schema(description = "User's first name", example = "Petar", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Lastname is mandatory")
    @Schema(description = "User's last name", example = "Petrović", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "username is mandatory")
    @Size(min = 2, max = 50, message = "username must be between 2 and 50 characters.")
    @Schema(description = "Unique account username (2 to 50 characters)", example = "petar_p", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Email(message = "Email is not valid")
    @NotBlank(message = "email cannot be empty")
    @Schema(description = "User's email address", example = "petar.petrovic@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "address is mandatory.")
    @Schema(description = "Residential or delivery address", example = "Vožda Karađorđa 15, Niš", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotBlank(message = "phone is mandatory.")
    @Schema(description = "Contact phone number", example = "+381641234567", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "User account password", example = "P@ssw0rd123!", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

}
