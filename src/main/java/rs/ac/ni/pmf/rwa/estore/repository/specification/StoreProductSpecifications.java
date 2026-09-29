package rs.ac.ni.pmf.rwa.estore.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreProductEntity;

import java.math.BigDecimal;

public class StoreProductSpecifications {

    public static Specification<StoreProductEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<StoreProductEntity> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) return cb.between(root.get("price"), minPrice, maxPrice);
            if (minPrice != null) return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<StoreProductEntity> hasStock(Integer stock) {
        return (root, query, cb) ->
                stock == null ? null : cb.equal(root.get("stock"), stock);
    }

    public static Specification<StoreProductEntity> stockBetween(Integer minStock, Integer maxStock) {
        return (root, query, cb) -> {
            if (minStock == null && maxStock == null) return null;
            if (minStock != null && maxStock != null) return cb.between(root.get("stock"), minStock, maxStock);
            if (minStock != null) return cb.greaterThanOrEqualTo(root.get("stock"), minStock);
            return cb.lessThanOrEqualTo(root.get("stock"), maxStock);
        };
    }

    public static Specification<StoreProductEntity> stockGreaterThan(Integer minStock) {
        return (root, query, cb) ->
                minStock == null ? null : cb.greaterThanOrEqualTo(root.get("stock"), minStock);
    }

}
