package rs.ac.ni.pmf.rwa.estore.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

public class StoreSpecifications {

    public static Specification<StoreEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<StoreEntity> hasAddress(String address) {
        return (root, query, cb) ->
                address == null ? null : cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<StoreEntity> hasActiveStatus(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? null : cb.equal(root.get("isActive"), isActive);
    }
}
