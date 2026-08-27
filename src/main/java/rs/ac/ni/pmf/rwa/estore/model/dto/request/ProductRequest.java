package rs.ac.ni.pmf.rwa.estore.model.dto.request;

import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductRequest {

    private Long id;
    private String name;
    private String type;
    private String description;
    private String barcode;
}
