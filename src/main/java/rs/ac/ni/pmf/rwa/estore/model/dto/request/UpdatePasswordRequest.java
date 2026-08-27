package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 64, message = "New password must be between 8 and 64 characters.")
    private String newPassword;

}