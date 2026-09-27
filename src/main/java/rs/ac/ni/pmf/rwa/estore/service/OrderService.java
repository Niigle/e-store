package rs.ac.ni.pmf.rwa.estore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.OrderItemMapper;
import rs.ac.ni.pmf.rwa.estore.mapper.OrderMapper;
import rs.ac.ni.pmf.rwa.estore.model.dto.request.AddToOrderRequest;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.OrderItemResponse;
import rs.ac.ni.pmf.rwa.estore.model.dto.response.OrderResponse;
import rs.ac.ni.pmf.rwa.estore.model.entity.OrderEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.OrderItemEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreProductEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.UserEntity;
import rs.ac.ni.pmf.rwa.estore.repository.OrderItemRepository;
import rs.ac.ni.pmf.rwa.estore.repository.OrderRepository;
import rs.ac.ni.pmf.rwa.estore.repository.StoreProductRepository;
import rs.ac.ni.pmf.rwa.estore.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String STATUS_COMPLETED = "COMPLETED";

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StoreProductRepository storeProductRepository;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    public OrderResponse addToOrder(AddToOrderRequest addToOrderRequest) {

        if (addToOrderRequest.getQuantity() == null || addToOrderRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be higher than zero");
        }

        StoreProductEntity storeProduct = storeProductRepository.findById(addToOrderRequest.getStoreProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + addToOrderRequest.getStoreProductId() + " not found"));

        if (storeProduct.getStock() < addToOrderRequest.getQuantity()) {
            throw new IllegalStateException("Not enough in stock (on stock: " + storeProduct.getStock() + ")");
        }

        OrderEntity orderEntity = orderRepository.findByUserIdAndStatus(addToOrderRequest.getUserId(), STATUS_IN_PROGRESS)
               .orElseGet(() -> createNewOrder(addToOrderRequest.getUserId()));

        OrderItemEntity existingItem = orderItemRepository
                .findByOrderIdAndStoreProductId(orderEntity.getId(), addToOrderRequest.getStoreProductId())
                .orElse(null);
                //.orElseThrow(() -> new ResourceNotFoundException("Order item id: " + addToOrderRequest.getStoreProductId() + " not found"));

        if (existingItem != null) {

            int newQuantity = existingItem.getQuantity() + addToOrderRequest.getQuantity();
            if (storeProduct.getStock() < newQuantity) {
                throw new IllegalStateException("Not enough in stock");
            }
            existingItem.setQuantity(newQuantity);
            orderItemRepository.save(existingItem);
        } else {
            OrderItemEntity newItem = OrderItemEntity.builder()
                                                    .order(orderEntity)
                                                    //.storeProduct(storeProduct)
                                                    .quantity(addToOrderRequest.getQuantity())
                                                    .priceAtPurchase(storeProduct.getPrice())
                                                    .build();

            newItem.setStoreProduct(storeProduct);
            orderItemRepository.save(newItem);
        }

        recalculateTotal(orderEntity);

        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        //orderResponse.

        return orderResponse;
        //return buildOrderResponse(orderEntity);
    }

    public Page<OrderResponse> getOrderHistory(Long userId, Pageable pageable) {

        //TODO & status=COMPLETED?
        Page<OrderEntity> orderEntityPage = orderRepository.findByUserId(userId, pageable);

        if (orderEntityPage.isEmpty()) {
            throw new ResourceNotFoundException("Order for user " + userId + " is empty");
        }

        Page<OrderResponse> orderResponsePage = orderEntityPage.map(orderEntity -> {
            List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderId(orderEntity.getId());

            List<OrderItemResponse> orderItemResponses = orderItemEntities.stream()
                    .map(orderItemMapper::toOrderItemResponse)
                    .toList();

            return orderMapper.toOrderResponse(orderEntity);
        });

        return orderResponsePage;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId) {

        OrderEntity orderEntity = orderRepository.findByUserIdAndStatus(userId, STATUS_IN_PROGRESS)
                .orElseThrow(() -> new ResourceNotFoundException("Order for user " + userId + " is empty"));

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderId(orderEntity.getId());

        List<OrderItemResponse> orderItemResponse = orderItemEntities.stream().map(orderItemMapper::toOrderItemResponse).toList();

        //OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderEntity.getId())
                .totalPrice(orderEntity.getTotal())
                .status(orderEntity.getStatus())
                .items(orderItemResponse)
                .build();

        return orderResponse;
        //return orderMapper.toOrderResponse(orderEntity);
        //return buildOrderResponse(orderEntity);

    }

    @Transactional
    public void removeItem(Long orderItemId) {
        OrderItemEntity orderItemEntity = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item id " + orderItemId + " not found"));

        OrderEntity orderEntity = orderItemEntity.getOrder();
        orderItemRepository.delete(orderItemEntity);
        recalculateTotal(orderEntity);
    }

    @Transactional
    public OrderResponse checkout(Long userId) {
        OrderEntity orderEntity = orderRepository.findByUserIdAndStatus(userId, STATUS_IN_PROGRESS)
                .orElseThrow(() -> new ResourceNotFoundException("Order for user: " + userId + " is empty"));

        List<OrderItemEntity> items = orderItemRepository.findByOrderId(orderEntity.getId());
        if (items.isEmpty()) {
            throw new IllegalStateException("Order is empty");
        }

        for (OrderItemEntity item : items) {

            StoreProductEntity storeProduct = item.getStoreProduct();

            if (storeProduct.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Not enoguh product: " + storeProduct.getName() + "  in stock.");
            }
        }

        for (OrderItemEntity item : items) {

            StoreProductEntity storeProduct = item.getStoreProduct();
            storeProduct.setStock(storeProduct.getStock() - item.getQuantity());
            storeProductRepository.save(storeProduct);
        }

        recalculateTotal(orderEntity);
        orderEntity.setStatus(STATUS_COMPLETED);
        orderRepository.save(orderEntity);

        return orderMapper.toOrderResponse(orderEntity);
        //return buildOrderResponse(orderEntity);
    }

    private OrderEntity createNewOrder(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + userId + " not found"));

        OrderEntity newOrderEntity = OrderEntity.builder()
                .user(user)
                .total(BigDecimal.ZERO)
                .status(STATUS_IN_PROGRESS)
                .build();

        return orderRepository.save(newOrderEntity);
    }

    private void recalculateTotal(OrderEntity orderEntity) {

        List<OrderItemEntity> items = orderItemRepository.findByOrderId(orderEntity.getId());

        BigDecimal total = items.stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orderEntity.setTotal(total);

        orderRepository.save(orderEntity);
    }
/*
    private OrderResponse buildOrderResponse(OrderEntity orderEntity) {

    }*/
}
