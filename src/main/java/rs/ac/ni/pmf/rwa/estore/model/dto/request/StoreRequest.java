package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Request payload for creating or updating a store")
public class StoreRequest {

    @NotBlank(message = "name cannot be blank")
    @Schema(description = "Store name", example = "TechZone Center", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "address cannot be blank")
    @Schema(description = "Physical address of the store", example = "Bulevar Nemanjića 12, Niš", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotNull(message = "categoryId cannot be blank")
    @Schema(description = "ID of the category the store belongs to", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    @NotNull(message = "managerId cannot be blank")
    @Schema(description = "ID of the user managing this store", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long managerId;

    @NotBlank(message = "phone cannot be blank")
    @Schema(description = "Contact phone number", example = "+38118123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "Whether the store is currently active", example = "true", defaultValue = "true")
    private Boolean isActive;
}
