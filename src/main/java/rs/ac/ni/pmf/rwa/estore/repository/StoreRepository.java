package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long>{

    List<StoreEntity> findByIsActive(Integer isActive);
    List<StoreEntity> findByManagerId(Long managerId);
}
