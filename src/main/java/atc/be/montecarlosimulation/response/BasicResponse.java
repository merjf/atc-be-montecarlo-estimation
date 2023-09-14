package atc.be.montecarlosimulation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicResponse {
    private Object body;
    private String message;
}
