package com.rudnev.visualizationsystembackend.service;

import com.rudnev.visualizationsystembackend.model.ReactorPoint;
import com.rudnev.visualizationsystembackend.model.ReactorRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactorService {
    public List<ReactorPoint> calculateAll(ReactorRequest request) {
        List<ReactorPoint> result = new ArrayList<>();

        double U = request.getU();
        double lMax = request.getLMax();
        double h = request.getH();
        double k1 = request.getK1();
        double k2 = request.getK2();
        double k3 = request.getK3();

        double Xa = request.getXa();
        double Xb = request.getXb();
        double Xc = request.getXc();
        double Xd = request.getXd();

        for (double l = 0.0; l <= lMax + 1e-10; l += h) {
            double sum = Xa + Xb + Xc + Xd;
            result.add(new ReactorPoint(l, Xa, Xb, Xc, Xd, sum));

            double Xa_star = Xa + h * (-(k1 + k2) * Xa + k3 * Xc) / U;
            double Xb_star = Xb + h * (k1 * Xa) / U;
            double Xc_star = Xc + h * (-k3 * Xc) / U;
            double Xd_star = Xd + h * (k2 * Xa) / U;

            Xa = Xa + (h / 2) * ((-(k1 + k2) * Xa + k3 * Xc) / U + (-(k1 + k2) * Xa_star + k3 * Xc_star) / U);
            Xb = Xb + (h / 2) * ((k1 * Xa) / U + (k1 * Xa_star) / U);
            Xc = Xc + (h / 2) * ((-k3 * Xc) / U + (-k3 * Xc_star) / U);
            Xd = Xd + (h / 2) * ((k2 * Xa) / U + (k2 * Xa_star) / U);

            double S = Xa + Xb + Xc + Xd;
            Xa /= S;
            Xb /= S;
            Xc /= S;
            Xd /= S;
        }

        return result;
    }

    public ReactorPoint calculateAtL(ReactorRequest request) {
        List<ReactorPoint> all = calculateAll(request);
        double targetL = request.getL() != null ? request.getL() : 0.0;
        ReactorPoint closest = all.get(0);

        for (ReactorPoint point : all) {
            if (Math.abs(point.getL() - targetL) < Math.abs(closest.getL() - targetL)) {
                closest = point;
            }
        }

        return closest;
    }
}
