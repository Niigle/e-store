package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class OrderResponse {

    private Long orderId;
    private String status;
    private BigDecimal totalPrice;
    private List<OrderItemResponse> items;
}
