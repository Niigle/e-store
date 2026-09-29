package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class AddToOrderRequest {

    private Long id;

    @NotNull(message = "userId is mandatory")
    private Long userId;


    @NotNull(message = "storeProductId is mandatory")
    private Long storeProductId;

    @NotNull(message = "quantity is mandatory")
    private Integer quantity;

    @NotNull(message = "price is mandatory")
    private BigDecimal priceAtPurchase;
}
