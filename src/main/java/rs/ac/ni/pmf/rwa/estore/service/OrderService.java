package rs.ac.ni.pmf.rwa.estore.service;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import rs.ac.ni.pmf.rwa.estore.exception.ResourceNotFoundException;
import rs.ac.ni.pmf.rwa.estore.mapper.OrderItemMapper;
import rs.ac.ni.pmf.rwa.estore.mapper.OrderMapper;
import rs.ac.ni.pmf.rwa.estore.messaging.OrderEventProducer;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String STATUS_COMPLETED = "COMPLETED";

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StoreProductRepository storeProductRepository;
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;
    private final OrderEventProducer orderEventProducer;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private Counter checkoutCounter;

    @PostConstruct
    public void init() {
        this.checkoutCounter = Counter.builder("orders.checkout.count")
                .description("Broj uspešno završenih kupovina")
                .register(meterRegistry);
    }

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
        /*OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderEntity.getId())
                .totalPrice(orderEntity.getTotal())
                .status(orderEntity.getStatus())
                .items(orderItemResponse)
                .build();*/

        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity)
                .toBuilder()
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

        log.info("Initiating checkout process for user ID: {}", userId);

        for (OrderItemEntity item : items) {

            StoreProductEntity storeProduct = item.getStoreProduct();

            if (storeProduct.getStock() < item.getQuantity()) {

                log.warn("Checkout failed for user ID: {}. Insufficient stock for product ID: {} (Requested: {}, Available: {})",
                        userId, storeProduct.getId(), item.getQuantity(), storeProduct.getStock());

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

        checkoutCounter.increment();

        Long orderId = orderEntity.getId();
        String email = orderEntity.getUser().getEmail();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                orderEventProducer.publishOrderCompleted(orderId, email);
            }
        });

        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);

        log.info("Order completed for user: {}. Order id: {}", userId, orderId);

        return orderResponse;
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

        OrderEntity orderEntity = orderRepository.save(newOrderEntity);
        log.info("New order for user: {} created, order id: {}", userId, orderEntity.getId());

        return orderEntity;
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
