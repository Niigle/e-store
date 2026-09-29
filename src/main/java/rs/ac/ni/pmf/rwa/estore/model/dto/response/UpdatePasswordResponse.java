package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Schema(description = "Response after password update")
public class UpdatePasswordResponse {

    @Schema(description = "Confirmation of successful password update")
    String message;
}
