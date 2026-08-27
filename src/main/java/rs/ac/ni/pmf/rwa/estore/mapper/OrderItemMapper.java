package rs.ac.ni.pmf.rwa.estore.mapper;

import org.springframework.stereotype.Component;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.AddToOrderRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.OrderItemResponse;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.OrderResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.OrderItemEntity;

@Component
public class OrderItemMapper {

    public OrderItemEntity toOrderItemEntity(AddToOrderRequest addToOrderRequest) {

        return OrderItemEntity.builder()
                .id(addToOrderRequest.getId())
                .quantity(addToOrderRequest.getQuantity())
                .priceAtPurchase(addToOrderRequest.getPriceAtPurchase())
                .build();

    }

    public OrderItemResponse toOrderItemResponse(OrderItemEntity orderItemEntity) {

        return OrderItemResponse.builder()
                .orderItemId(orderItemEntity.getId())
                .quantity(orderItemEntity.getQuantity())
                .priceAtPurchase(orderItemEntity.getPriceAtPurchase())
                .build();
    }

}
