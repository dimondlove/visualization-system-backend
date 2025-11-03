package com.rudnev.visualizationsystembackend.model;

import lombok.Data;

@Data
public class ReactorRequest {
    private double U;
    private double lMax;
    private double h;
    private double k1;
    private double k2;
    private double k3;
    private double Xa;
    private double Xb;
    private double Xc;
    private double Xd;
    private Double L; // используется только для расчета при заданном L
}
