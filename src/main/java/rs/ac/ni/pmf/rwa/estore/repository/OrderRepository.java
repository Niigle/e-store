package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import rs.ac.ni.pmf.rwa.estore.model.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository extends JpaRepositoryImplementation<OrderEntity, Long> {

    Optional<OrderEntity> findByUserIdAndStatus(Long userId, String status);

    Page<OrderEntity> findByUserId(Long userId, Pageable pageable);
}
