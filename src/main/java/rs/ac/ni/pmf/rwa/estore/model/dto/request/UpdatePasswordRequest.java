package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Schema(description = "Request for password update")
public class UpdatePasswordRequest {

    @Schema(description = "Old password", example = "OldPassword321!")
    @NotBlank(message = "oldPassword cannot be blank")
    private String oldPassword;

    @Schema(description = "New password", example = "NewPassword456$")
    @NotBlank(message = "newPassword cannot be blank")
    @Size(min = 8, max = 64, message = "New password must be between 8 and 64 characters.")
    private String newPassword;

}