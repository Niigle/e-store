package rs.ac.ni.pmf.rwa.estore.model.dto.event;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEvent implements Serializable {

    Long orderId;
    String userEmail;

}
