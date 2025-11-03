package com.rudnev.visualizationsystembackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReactorRequest {
    @JsonProperty("U")
    private double U;

    @JsonProperty("lMax")
    private double lMax;

    @JsonProperty("h")
    private double h;

    @JsonProperty("k1")
    private double k1;

    @JsonProperty("k2")
    private double k2;

    @JsonProperty("k3")
    private double k3;

    @JsonProperty("Xa")
    private double Xa;

    @JsonProperty("Xb")
    private double Xb;

    @JsonProperty("Xc")
    private double Xc;

    @JsonProperty("Xd")
    private double Xd;

    @JsonProperty("L")
    private Double L; // для расчета при заданном L
}
