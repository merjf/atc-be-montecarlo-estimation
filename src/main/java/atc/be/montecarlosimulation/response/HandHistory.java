package atc.be.montecarlosimulation.response;

import lombok.Data;

import java.util.Date;

@Data
public class HandHistory {
    private String id;
    private float win;
    private float lose;
    private float spare;
    private int samples;
    private Date creationDate;
}
