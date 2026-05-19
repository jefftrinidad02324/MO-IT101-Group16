package com.mycompany.motorphpayroll;

public class Utility {

    public static String[] manualSplit(String line) {

        String[] arr = new String[19];
        String temp = "";
        int i = 0;
        boolean q = false;

        for (char c : line.toCharArray()) {

            if (c == '"') q = !q;

            else if (c == ',' && !q) {
                arr[i++] = temp;
                temp = "";
            } else {
                temp += c;
            }
        }

        arr[i] = temp;
        return arr;
    }

    public static double[] computeHours(String in, String out) {

        String[] i = in.split(":");
        String[] o = out.split(":");

        double start = Integer.parseInt(i[0]) + Integer.parseInt(i[1]) / 60.0;
        double end = Integer.parseInt(o[0]) + Integer.parseInt(o[1]) / 60.0;

        return new double[]{Math.max(0, end - start - 1)};
    }

    public static double getSSS(double salary) {
        if (salary <= 3250) return 135;
        if (salary <= 3750) return 157.5;
        if (salary <= 4250) return 180;
        return 1125;
    }

    public static double getTax(double income) {
        if (income <= 20832) return 0;
        if (income <= 33333) return (income - 20833) * 0.2;
        return 2500 + (income - 33333) * 0.25;
    }
}