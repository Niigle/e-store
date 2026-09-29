package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class StoreRequest {

    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotNull(message = "categoryId cannot be blank")
    private Long categoryId;

    @NotNull(message = "managerId cannot be blank")
    private Long managerId;

    @NotBlank(message = "phone cannot be blank")
    private String phone;
    
    private Boolean isActive;
}
