package rs.ac.ni.pmf.rwa.estore.model.dto;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Map;

@Value
@Builder
public class ErrorDto {

    String message;
    String path;
    OffsetDateTime timestamp;
    int status;
    String error;
    Map<String, String> validationErrors;

}
