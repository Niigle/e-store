package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class StoreResponse {

    private Long id;
    private String name;
    private String address;
    private String type;
    private String phone;
    private Integer isActive;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedOn;

}
