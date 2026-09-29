package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Schema(description = "Response payload containing user profile details")
public class UserResponse {

    @Schema(description = "Unique user ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "User's first name", example = "Petar")
    private String firstName;

    @Schema(description = "User's last name", example = "Petrović")
    private String lastName;

    @Schema(description = "Unique account username", example = "petar_p")
    private String username;

    @Schema(description = "User's email address", example = "petar.petrovic@example.com")
    private String email;

    @Schema(description = "Residential or delivery address", example = "Vožda Karađorđa 15, Niš")
    private String address;

    @Schema(description = "Contact phone number", example = "+381641234567")
    private String phoneNumber;

}