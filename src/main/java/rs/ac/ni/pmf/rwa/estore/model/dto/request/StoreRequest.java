package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class StoreRequest {

    private String name;
    private String address;
    private String type;
    private String phone;
    private Integer isActive;
}
