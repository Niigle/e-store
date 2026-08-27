package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class StoreProductRequest {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock = 0;
}
