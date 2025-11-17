package com.rudnev.visualizationsystembackend.model;

import lombok.Data;

import java.util.List;

@Data
public class TaskThirdResponse {
    private List<Double> t;
    private List<Double> y_e;
    private List<Double> u;
    private List<Double> a;
    private double b;
    private List<Double> y_r;
    private double F;
    private String text;
}
