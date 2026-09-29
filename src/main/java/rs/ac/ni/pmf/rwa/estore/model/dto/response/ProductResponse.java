package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Schema(description = "Response payload containing product details")
public class ProductResponse {

    @Schema(description = "Unique product ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Product name", example = "Wireless Ergonomic Mouse")
    private String name;

    @Schema(description = "Product category or type", example = "Electronics")
    private String type;

    @Schema(description = "Detailed product description", example = "High-precision optical mouse with rechargeable battery")
    private String description;

    @Schema(description = "Unique product barcode", example = "8600123456789")
    private String barcode;
}
