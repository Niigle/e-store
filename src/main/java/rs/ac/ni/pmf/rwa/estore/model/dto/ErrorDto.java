package rs.ac.ni.pmf.rwa.estore.model.dto;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class ErrorDto {

    String message;
    String path;
    OffsetDateTime timestamp;

}
