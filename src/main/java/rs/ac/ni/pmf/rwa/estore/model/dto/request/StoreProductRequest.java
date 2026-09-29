package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import rs.ac.ni.pmf.rwa.estore.model.entity.ProductEntity;
import rs.ac.ni.pmf.rwa.estore.model.entity.StoreEntity;

import java.math.BigDecimal;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
@Schema(description = "Request for updating stock and price of products at store")
public class StoreProductRequest {

    @Schema(description = "ID of store product", example = "1")
    Long id;

    @Schema(description = "Product name", example = "Milk 1l")
    String name;

    @DecimalMin(value = "0.0", message = "Price can't be negative")
    @Schema(description = "Product price at the store", example = "149.99")
    @NotNull(message = "price is mandatory")
    BigDecimal price;

    @Min(value = 0, message = "Stock can't be negative")
    @Schema(description = "Quantity at stock", example = "25")
    @NotNull(message = "stock is mandatory")
    Integer stock;
}
