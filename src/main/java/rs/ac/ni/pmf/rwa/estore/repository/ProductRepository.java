package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findById(Long id);
    Optional<ProductEntity> findByBarcode(String barcode);
}
