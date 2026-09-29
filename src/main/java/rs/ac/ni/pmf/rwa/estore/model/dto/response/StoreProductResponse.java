package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Product from a store with price and stock")
public class StoreProductResponse {

    @Schema(description = "Unique ID of a store product", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Milk 1l")
    private String name;

    @Schema(description = "Prouduct price at the store", example = "149.99")
    private BigDecimal price;

    @Schema(description = "Quantity at stock", example = "25")
    private Integer stock;

}
