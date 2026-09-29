package rs.ac.ni.pmf.rwa.estore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long>, JpaSpecificationExecutor<StoreEntity> {

    List<StoreEntity> findByIsActive(Boolean isActive);
    List<StoreEntity> findByManager_Id(Long managerId);
}
