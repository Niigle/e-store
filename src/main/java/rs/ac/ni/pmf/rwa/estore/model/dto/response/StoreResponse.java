package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Response payload containing store details")
public class StoreResponse {

    @Schema(description = "Unique store ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Store name", example = "TechZone Center")
    private String name;

    @Schema(description = "Physical address of the store", example = "Bulevar Nemanjića 12, Niš")
    private String address;

    @Schema(description = "Contact phone number", example = "+38118123456")
    private String phone;

    @Schema(description = "Whether the store is currently active", example = "true")
    private Boolean isActive;

    @Schema(description = "Timestamp when the store record was created", example = "2026-03-30T10:15:30")
    private LocalDateTime createdOn;

    @Schema(description = "Timestamp when the store record was last modified", example = "2026-03-30T14:20:00")
    private LocalDateTime modifiedOn;

}
