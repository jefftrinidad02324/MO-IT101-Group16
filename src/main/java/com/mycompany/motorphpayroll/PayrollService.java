package com.mycompany.motorphpayroll;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PayrollService {

    private static final String EMPLOYEE_DETAILS_FILE =
            "src/main/java/com/mycompany/motorphpayroll/Employee Details.csv";

    private static final String ATTENDANCE_RECORD_FILE =
            "src/main/java/com/mycompany/motorphpayroll/Employee Attendance Record.csv";

    private static final double START_WORK = 8.0;
    private static final double END_WORK = 17.0;

    // ================= PROCESS ONE =================
    public static void processOne(String employeeId, JTextField nameField, JTextArea outputArea) {
        outputArea.setText("");
        calculate(employeeId, nameField, outputArea);
    }

    // ================= PROCESS ALL =================
    public static void processAll(JTextArea outputArea) {
        outputArea.setText("");

        for (int id = 10001; id <= 10034; id++) {
            calculate(String.valueOf(id), null, outputArea);
        }

        outputArea.append("\n====================================================\n");
        outputArea.append("ALL PAYROLL PROCESSING COMPLETE\n");
        outputArea.append("====================================================\n");
    }

    // ================= MAIN CALCULATION =================
    private static boolean calculate(String employeeId, JTextField nameField, JTextArea outputArea) {

        try {
            String name = "";
            String birthday = "";
            double basicSalary = 0;
            double hourlyRate = 0;

            boolean found = false;

            // ================= READ EMPLOYEE FILE =================
            try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_FILE))) {

                String line;
                br.readLine();

                while ((line = br.readLine()) != null) {

                    if (line.trim().isEmpty()) continue;

                    String[] data = manualSplit(line);

                    if (data[0].trim().equals(employeeId.trim())) {

                        name = data[2].trim() + " " + data[1].trim();
                        birthday = data[3].trim();

                        basicSalary = parseDoubleSafe(data[13]);
                        hourlyRate = parseDoubleSafe(data[18]);

                        found = true;

                        if (nameField != null) {
                            nameField.setText(name);
                        }
                        break;
                    }
                }
            }

            if (!found) {
                outputArea.append("\nEmployee not found: " + employeeId);
                return false;
            }

            // ================= LOAD ATTENDANCE =================
            List<String[]> records = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_RECORD_FILE))) {

                String line;
                br.readLine();

                while ((line = br.readLine()) != null) {
                    records.add(line.split(","));
                }
            }

            boolean hasData = false;

            // ================= MONTH LOOP (JUNE–DECEMBER ONLY) =================
            for (int month = 6; month <= 12; month++) {

                double cutoff1 = 0;
                double cutoff2 = 0;
                double cutoff1Late = 0;
                double cutoff2Late = 0;

                for (String[] r : records) {

                    if (!r[0].trim().equals(employeeId.trim())) continue;

                    String[] date = r[3].split("/");
                    int m = Integer.parseInt(date[0]);
                    int day = Integer.parseInt(date[1]);

                    if (m != month) continue;

                    double[] work = computeDailyWork(r[4], r[5]);

                    if (day <= 15) {
                        cutoff1 += work[0];
                        cutoff1Late += work[1];
                    } else {
                        cutoff2 += work[0];
                        cutoff2Late += work[1];
                    }
                }

                if (cutoff1 == 0 && cutoff2 == 0) continue;

                hasData = true;

                // ================= COMPUTATION (CORRECTED BASED ON YOUR REFERENCE) =================

                double firstCutoffSalary = cutoff1 * hourlyRate;
                double secondCutoffSalary = cutoff2 * hourlyRate;

                double totalGrossSalary = firstCutoffSalary + secondCutoffSalary;

                // DEDUCTIONS BASED ON BASIC SALARY
                double sss = getSSS(basicSalary);
                double phil = (basicSalary * 0.03) / 2;

                double pagibig =
                        (basicSalary <= 1500)
                                ? basicSalary * 0.01
                                : basicSalary * 0.02;

                double taxable = Math.max(0,
                        totalGrossSalary - (sss + phil + pagibig));

                double tax = getTax(taxable);

                double totalDeductions = sss + phil + pagibig + tax;

                double netSalarySecondCutoff = secondCutoffSalary - totalDeductions;

                // ================= OUTPUT =================

                outputArea.append("\n========================================\n");
                outputArea.append("Employee number: " + employeeId + "\n");
                outputArea.append("Employee name: " + name + "\n");
                outputArea.append("Birthday: " + birthday + "\n");

                outputArea.append("\nCutoff date: 1 - 15 (" + getMonthName(month) + ")\n");
                outputArea.append("Total hours worked: " + cutoff1 + "\n");
                outputArea.append("Late hours: " + cutoff1Late + "\n");
                outputArea.append("Gross salary: " + firstCutoffSalary + "\n");
                outputArea.append("Net salary: " + firstCutoffSalary + "\n");

                outputArea.append("\nCutoff date: 16 - End (" + getMonthName(month) + ")\n");
                outputArea.append("Total hours worked: " + cutoff2 + "\n");
                outputArea.append("Late hours: " + cutoff2Late + "\n");
                outputArea.append("Gross salary: " + secondCutoffSalary + "\n");

                outputArea.append("\nSSS: " + sss + "\n");
                outputArea.append("Phil-health: " + phil + "\n");
                outputArea.append("Pag-ibig: " + pagibig + "\n");
                outputArea.append("Tax: " + tax + "\n");

                outputArea.append("Total deductions: " + totalDeductions + "\n");
                outputArea.append("Net salary: " + netSalarySecondCutoff + "\n");

                outputArea.append("========================================\n");
            }

            if (!hasData) {
                outputArea.append("\nNo records found (June–December only).");
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            outputArea.append("\nERROR: " + e.getMessage());
            return false;
        }
    }

    // ================= TIME COMPUTATION =================
    private static double[] computeDailyWork(String in, String out) {

        String[] i = in.split(":");
        String[] o = out.split(":");

        double start = Integer.parseInt(i[0]) + Integer.parseInt(i[1]) / 60.0;
        double end = Integer.parseInt(o[0]) + Integer.parseInt(o[1]) / 60.0;

        start = Math.max(8.0, start);
        end = Math.min(17.0, end);

        double late = 0;

        int h = Integer.parseInt(i[0]);
        int m = Integer.parseInt(i[1]);

        if (h > 8 || (h == 8 && m >= 11)) {
            late = 0.5;
        }

        double hours = end - start;
        hours -= 1;

        return new double[]{
                Math.max(0, hours),
                late
        };
    }

    // ================= HELPERS =================
    private static double parseDoubleSafe(String v) {
        try {
            return Double.parseDouble(v.replace(",", "").replace("\"", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private static String getMonthName(int m) {
        switch (m) {
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "";
        }
    }

    // ================= DEDUCTIONS =================
    private static double getSSS(double salary) {
        if (salary <= 3250) return 135;
        if (salary <= 3750) return 157.5;
        if (salary <= 4250) return 180;
        if (salary <= 10000) return 450;
        if (salary <= 20000) return 900;
        return 1125;
    }

    private static double getTax(double income) {
        if (income <= 20833) return 0;
        if (income <= 33333) return (income - 20833) * 0.20;
        if (income <= 66667) return 2500 + (income - 33333) * 0.25;
        if (income <= 166667) return 10833.33 + (income - 66667) * 0.30;
        return 40833.33 + (income - 166667) * 0.35;
    }

    // ================= CSV PARSER =================
    public static String[] manualSplit(String line) {

        String[] result = new String[19];
        String current = "";
        int idx = 0;
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {

            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                result[idx++] = current;
                current = "";
            } else {
                current += c;
            }
        }

        result[idx] = current;
        return result;
    }
}