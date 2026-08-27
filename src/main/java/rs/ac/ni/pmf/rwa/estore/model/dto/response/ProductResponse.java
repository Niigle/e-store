package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String type;
    private String description;
    private String barcode;
}
