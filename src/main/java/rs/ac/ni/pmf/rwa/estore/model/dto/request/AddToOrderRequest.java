package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class AddToOrderRequest {

    private Long id;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long storeProductId;

    @NotBlank
    private Integer quantity;

    @NotBlank
    private BigDecimal priceAtPurchase;
}
