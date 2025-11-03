package com.rudnev.visualizationsystembackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactorPoint {
    private double L;
    private double Xa;
    private double Xb;
    private double Xc;
    private double Xd;
    private double sum;
}
