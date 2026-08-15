package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdatePasswordRequest {

    private String oldPassword;
    private String newPassword;

}