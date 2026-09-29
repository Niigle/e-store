package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductRequest {

    private Long id;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "type is mandatory")
    private String type;

    private String description;

    private String image;

    @NotBlank(message = "barcode is mandatory")
    private String barcode;
}
