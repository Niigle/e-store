package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreProductEntity;

import java.util.List;
import java.util.Optional;

public interface StoreProductRepository extends JpaRepository<StoreProductEntity, Long>, JpaSpecificationExecutor<StoreProductEntity> {
    List<StoreProductEntity> findByStoreId(Long storeId);
    List<StoreProductEntity> findByProductId(Long productId);
    StoreProductEntity findByStoreIdAndProductId(Long storeId, Long productId);
}
