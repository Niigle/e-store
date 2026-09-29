package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Response payload containing details of an item within an order")
public class OrderItemResponse {

    @Schema(description = "Unique order item ID", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long orderItemId;

    @Schema(description = "ID of the ordered store product", example = "100")
    private Long storeProductId;

    @Schema(description = "Name of the ordered product", example = "Wireless Ergonomic Mouse")
    private String productName;

    @Schema(description = "Quantity ordered", example = "2")
    private Integer quantity;

    @Schema(description = "Price per unit at the moment of purchase", example = "1299.99")
    private BigDecimal priceAtPurchase;
}
