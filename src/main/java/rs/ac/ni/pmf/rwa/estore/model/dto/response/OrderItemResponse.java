package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class OrderItemResponse {

    private Long orderItemId;
    private Long storeProductId;
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
