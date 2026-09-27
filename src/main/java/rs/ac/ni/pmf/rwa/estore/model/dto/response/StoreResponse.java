package rs.ac.ni.pmf.rwa.estore.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor()
public class StoreResponse {

    private Long id;
    private String name;
    private String address;
    private String type;
    private String phone;
    //TODO
    private Boolean isActive;
    //private Category category;
    private String managerId;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

}
