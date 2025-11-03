package com.rudnev.visualizationsystembackend.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "L", "Xa", "Xb", "Xc", "Xd", "sum" })
public class ReactorPoint {
    private double L;
    private double Xa;
    private double Xb;
    private double Xc;
    private double Xd;
    private double sum;
}
