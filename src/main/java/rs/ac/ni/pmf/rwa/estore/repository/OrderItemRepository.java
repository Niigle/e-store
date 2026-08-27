package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ni.pmf.rwa.estore.model.entity.OrderItemEntity;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    List<OrderItemEntity> findByOrderId(Long orderId);
    Optional<OrderItemEntity> findByOrderIdAndStoreProductId(Long orderId, Long storeProductId);
}
