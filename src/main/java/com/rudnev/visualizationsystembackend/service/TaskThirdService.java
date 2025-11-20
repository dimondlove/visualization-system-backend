package com.rudnev.visualizationsystembackend.service;

import com.rudnev.visualizationsystembackend.model.TaskThirdResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskThirdService {
    public TaskThirdResponse process(MultipartFile file, int d, int Ne, int Nu, double Ftable, int method) throws Exception {
        List<Double> tList = new ArrayList<>();
        List<Double> yEList = new ArrayList<>();
        List<Double> uList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (!headerSkipped && (line.toLowerCase().startsWith("t,") || line.toLowerCase().startsWith("t;") || line.toLowerCase().startsWith("t\t"))) {
                    headerSkipped = true;
                    continue;
                }
                headerSkipped = true;
                String[] parts = line.split("[,;\\t]");
                if (parts.length < 3) throw new IllegalArgumentException("CSV must have 3 columns: t,y_e,u");
                double tv = Double.parseDouble(parts[0].trim().replace(',', '.'));
                double yv = Double.parseDouble(parts[1].trim().replace(',', '.'));
                double uv = Double.parseDouble(parts[2].trim().replace(',', '.'));
                tList.add(tv);
                yEList.add(yv);
                uList.add(uv);
            }
        }

        if (tList.size() == 0) throw new IllegalArgumentException("CSV is empty or invalid.");

        int m = tList.size();
        int pad = d + 5;
        int arrLen = m + pad + 5;
        int N = Ne + Nu;
        double[] T = new double[arrLen];
        double[] y_e = new double[arrLen];
        double[] u = new double[arrLen];

        Arrays.fill(T, 0.0);
        Arrays.fill(y_e, 0.0);
        Arrays.fill(u, 0.0);

        for (int i = 0; i < m; i++) {
            T[i] = tList.get(i);
            y_e[i] = yEList.get(i);
            u[i] = uList.get(i);
        }

        int i_0 = d + 1;

        int n = (method == 3) ? 3 : 2;

        ComputationResult comp = computeEquation(u, y_e, d, i_0, N, Ftable, n);

        TaskThirdResponse resp = new TaskThirdResponse();
        resp.setT(toList(T, m));
        resp.setY_e(toList(y_e, m));
        resp.setU(toList(u, m));
        resp.setA(toList(comp.a, comp.a.length));
        resp.setB(comp.b);
        resp.setY_r(toList(comp.y_r, m));
        resp.setF(comp.F);
        resp.setText(comp.text);
        return resp;
    }

    private List<Double> toList(double[] arr, int m) {
        List<Double> out = new ArrayList<>(m);
        for (int i = 0; i < m; i++) out.add(arr[i]);
        return out;
    }

    private static class ComputationResult {
        double[] a;
        double b;
        double[] y_r;
        double F;
        String text;
    }

    private ComputationResult computeEquation(double[] u, double[] y_e, int d, int i_0, int N, double F_table, int n) {
        double[][] XY;
        if (n == 3) XY = new double[4][5];
        else XY = new double[3][4];

        int upper = i_0 + N;
        if (upper >= y_e.length) upper = y_e.length - 1;

        for (int i = i_0 + 1; i <= upper; i++) {
            if (n == 3) {
                XY[0][0] += Math.pow(y_e[i - 1], 2.0);
                XY[0][1] += y_e[i - 2] * y_e[i - 1];
                XY[0][2] += y_e[i - 3] * y_e[i - 1];
                XY[0][3] += u[i - 1 - d] * y_e[i - 1];
                XY[0][4] += y_e[i] * y_e[i - 1];

                XY[1][0] += y_e[i - 1] * y_e[i - 2];
                XY[1][1] += Math.pow(y_e[i - 2], 2.0);
                XY[1][2] += y_e[i - 3] * y_e[i - 2];
                XY[1][3] += u[i - 1 - d] * y_e[i - 2];
                XY[1][4] += y_e[i] * y_e[i - 2];

                XY[2][0] += y_e[i - 1] * y_e[i - 3];
                XY[2][1] += y_e[i - 2] * y_e[i - 3];
                XY[2][2] += Math.pow(y_e[i - 3], 2.0);
                XY[2][3] += u[i - 1 - d] * y_e[i - 3];
                XY[2][4] += y_e[i] * y_e[i - 3];

                XY[3][0] += y_e[i - 1] * u[i - 1 - d];
                XY[3][1] += y_e[i - 2] * u[i - 1 - d];
                XY[3][2] += y_e[i - 3] * u[i - 1 - d];
                XY[3][3] += Math.pow(u[i - 1 - d], 2.0);
                XY[3][4] += y_e[i] * u[i - 1 - d];
            } else {
                XY[0][0] += Math.pow(y_e[i - 1], 2.0);
                XY[0][1] += y_e[i - 2] * y_e[i - 1];
                XY[0][2] += u[i - 1 - d] * y_e[i - 1];
                XY[0][3] += y_e[i] * y_e[i - 1];

                XY[1][0] += y_e[i - 1] * y_e[i - 2];
                XY[1][1] += Math.pow(y_e[i - 2], 2.0);
                XY[1][2] += u[i - 1 - d] * y_e[i - 2];
                XY[1][3] += y_e[i] * y_e[i - 2];

                XY[2][0] += y_e[i - 1] * u[i - 1 - d];
                XY[2][1] += y_e[i - 2] * u[i - 1 - d];
                XY[2][2] += Math.pow(u[i - 1 - d], 2.0);
                XY[2][3] += y_e[i] * u[i - 1 - d];
            }
        }

        double[] coefficients = gaussMethod(XY, n);

        ComputationResult result = new ComputationResult();
        if (n == 3) {
            result.a = new double[]{coefficients[0], coefficients[1], coefficients[2]};
            result.b = coefficients[3];
        } else {
            result.a = new double[]{coefficients[0], coefficients[1]};
            result.b = coefficients[2];
        }

        result.y_r = calculationY_r(i_0, y_e, u, N, d, result.a, result.b, n);

        result.F = calculationFisherCriterion(result.y_r, y_e, N, i_0, n);

        if (result.F > F_table) result.text = "F > F_table -> модель адекватна объекту";
        else result.text = "F <= F_table -> модель не адекватна объекту";

        return result;
    }

    private double[] gaussMethod(double[][] XY, int n) {
        double[] coefficients = new double[n + 1];
        double m;

        for (int k = 1; k < XY.length; k++) {
            for (int j = k; j < XY.length; j++) {
                m = XY[j][k - 1] / XY[k - 1][k - 1];
                for (int i = 0; i < XY.length + 1; i++) {
                    XY[j][i] = XY[j][i] - m * XY[k - 1][i];
                }
            }

            for (int i = XY.length - 1; i >= 0; i--) {
                coefficients[i] = XY[i][XY.length] / XY[i][i];
                for (int c = XY.length - 1; c > i; c--) {
                    coefficients[i] = coefficients[i] - XY[i][c] * coefficients[c] / XY[i][i];
                }
            }
        }

        return coefficients;
    }

    private double[] calculationY_r (int i_0, double[] y_e, double[] u, int N, int d, double[] a, double b, int n) {
        double[] y_r = new double[y_e.length];

        for (int i = 0; i <= i_0; i++)
            y_r[i] = 0.0;

        for (int i = i_0 + 1; i <= N + i_0; i++) {
            for (int j = 1; j <= n; j++)
                y_r[i] += a[j - 1] * y_e[i - j];
            y_r[i] += b * u[i - 1 - d];
        }

        for (int i = N + i_0 + 1; i < y_e.length; i++)
            y_r[i] = y_r[N + i_0];

        return y_r;
    }

    private double calculationFisherCriterion(double[] y_r, double[] y_e, int N, int i_0, int n) {
        double S_2y = 0.0;
        double y_e_sr = 0.0;

        for (int i = i_0 + 1; i <= N + i_0; i++)
            y_e_sr += y_e[i];

        y_e_sr /= N;

        for (int i = i_0 + 1; i <= N + i_0; i++)
            S_2y += Math.pow(y_e[i] - y_e_sr, 2.0);

        S_2y /= N - 1;

        double S_2ost = 0.0;

        for (int i = i_0 + 1; i <= N + i_0; i++)
            S_2ost += Math.pow(y_r[i] - y_e[i], 2.0);

        S_2ost /= N - n - 1;

        return S_2y /  S_2ost;
    }
}
