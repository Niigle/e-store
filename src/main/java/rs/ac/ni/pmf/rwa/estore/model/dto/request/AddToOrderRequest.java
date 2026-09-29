package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Request payload for adding an item to a shopping cart or order")
public class AddToOrderRequest {

    private Long id;

    @NotNull(message = "userId is mandatory")
    @Schema(description = "ID of the user making the order", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotNull(message = "storeProductId is mandatory")
    @Schema(description = "ID of the product in the specific store", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long storeProductId;

    @NotNull(message = "quantity is mandatory")
    @Schema(description = "Quantity of the product to order", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @NotNull(message = "price is mandatory")
    @Schema(description = "Price per unit at the moment of purchase", example = "1299.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal priceAtPurchase;
}
