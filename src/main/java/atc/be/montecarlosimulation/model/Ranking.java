package atc.be.montecarlosimulation.model;

import lombok.Data;

import java.util.List;

@Data
public class Ranking {
    int ranking1lvl;
    List<Integer> ranking2lvl;
    int ranking3lvl;
}
