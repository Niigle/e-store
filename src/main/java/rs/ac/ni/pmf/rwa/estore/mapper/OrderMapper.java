package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.OrderResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.OrderEntity;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(OrderEntity orderEntity) {

        return OrderResponse.builder()
                .orderId(orderEntity.getId())
                .totalPrice(orderEntity.getTotal())
                .status(orderEntity.getStatus())
                .build();
    }

}
