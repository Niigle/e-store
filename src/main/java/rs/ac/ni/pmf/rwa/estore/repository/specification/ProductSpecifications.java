package rs.ac.ni.pmf.rwa.estore.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;

public class ProductSpecifications {

    public static Specification<ProductEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> hasType(String type) {
        return (root, query, cb) ->
                type == null ? null : cb.equal(root.get("type"), type);
    }
}
