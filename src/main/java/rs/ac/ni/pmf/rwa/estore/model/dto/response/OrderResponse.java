package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder(toBuilder = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Response payload containing complete order details")
public class OrderResponse {

    @Schema(description = "Unique order ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long orderId;

    @Schema(description = "Current status of the order", example = "PENDING", allowableValues = {"PENDING", "COMPLETED", "CANCELLED"})
    private String status;

    @Schema(description = "Total calculated price of the order", example = "2599.98")
    private BigDecimal totalPrice;

    @Schema(description = "List of items included in the order")
    private List<OrderItemResponse> items;
}
