package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import lombok.*;

@Value
@Getter
@Setter
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String password;

}
