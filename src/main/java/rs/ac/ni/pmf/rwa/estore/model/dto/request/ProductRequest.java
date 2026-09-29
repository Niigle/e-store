package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Schema(description = "Request payload for creating or updating a product")
public class ProductRequest {

    private Long id;

    @NotBlank(message = "name is mandatory")
    @Schema(description = "Product name", example = "Wireless Ergonomic Mouse", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "type is mandatory")
    @Schema(description = "Product category or type", example = "Electronics", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "Detailed product description", example = "High-precision optical mouse with rechargeable battery")
    private String description;

    @Schema(description = "URL or path to the product image", example = "https://example.com/images/mouse.jpg")
    private String image;

    @NotBlank(message = "barcode is mandatory")
    @Schema(description = "Unique product barcode", example = "8600123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    private String barcode;
}
