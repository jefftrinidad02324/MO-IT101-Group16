/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.motorphpayroll;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class MotorPHPayroll {

    // File paths for employee details and attendance records
    static String employeeDetailsFile = "src/main/java/com/mycompany/motorphpayroll/Employee Details.csv";
    static String attendanceRecordFile = "src/main/java/com/mycompany/motorphpayroll/Employee Attendance Record.csv";

    public static void main(String[] args) {
        // Scanner is used to get input from the user
        Scanner scanner = new Scanner(System.in);

        // Start login process
        handleLogin(scanner);

        // Close scanner after use to avoid memory leaks
        scanner.close();
    }

    public static void handleLogin(Scanner scanner) {
        System.out.println("--- MotorPH Login ---");

        // Ask user for login credentials
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        // Check if login is for regular employee
        if (username.equals("employee") && password.equals("12345")) {

            System.out.println("\n1. Enter your employee number\n2. Exit program");

            // Get user choice
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter ID: ");

                // Display employee info based on entered ID
                displayEmployeeInfo(scanner.next());
            }

        // Check if login is for payroll staff
        } else if (username.equals("payroll staff") && password.equals("12345")) {

            System.out.println("\n1. Process Payroll\n2. Exit");

            int menuChoice = scanner.nextInt();

            if (menuChoice == 1) {

                System.out.println("\n1. Process One Employee\n2. Process All Employees");
                int subMenuChoice = scanner.nextInt();

                // Process payroll for a single employee
                if (subMenuChoice == 1) {
                    System.out.print("Enter Employee ID: ");
                    calculatePayroll(scanner.next());

                // Process payroll for all employees (looping through IDs)
                } else if (subMenuChoice == 2) {
                    for (int employeeId = 10001; employeeId <= 10034; employeeId++) {
                        calculatePayroll(String.valueOf(employeeId));
                    }
                }
            }

        } else {
            // Invalid login message
            System.out.println("Invalid Login.");
        }
    }

    public static void displayEmployeeInfo(String employeeId) {

        // Try-with-resources automatically closes reader after use
        try (BufferedReader reader = new BufferedReader(new FileReader(employeeDetailsFile))) {

            String line;

            // Skip header row
            reader.readLine();

            // Read file line by line
            while ((line = reader.readLine()) != null) {

                // Split CSV line into array (custom method to handle quotes properly)
                String[] employeeData = manualSplit(line);

                // Compare employee ID
                if (employeeData[0].trim().equals(employeeId.trim())) {

                    System.out.println("\n--- Employee Details ---");
                    System.out.println("Employee number: " + employeeData[0]);

                    // Combine first name and last name
                    System.out.println("Employee name: " + employeeData[2] + " " + employeeData[1]);

                    System.out.println("Birthday: " + employeeData[3]);
                    return; // Stop once found
                }
            }

            // If not found
            System.out.println("Employee number does not exist.");

        } catch (Exception e) {
            System.out.println("Error reading file.");
        }
    }

    public static void calculatePayroll(String employeeId) {
        try {
            // Variables to store employee information
            String employeeName = "";
            double basicSalary = 0;
            double hourlyRate = 0;
            boolean employeeFound = false;

            // Read employee details file
            BufferedReader employeeReader = new BufferedReader(new FileReader(employeeDetailsFile));
            String line;

            // Skip header
            employeeReader.readLine();

            while ((line = employeeReader.readLine()) != null) {

                String[] employeeData = manualSplit(line);

                // Check if employee ID matches
                if (employeeData[0].trim().equals(employeeId.trim())) {

                    // Extract needed data
                    employeeName = employeeData[2] + " " + employeeData[1];

                    // Remove quotes and commas before converting to double
                    basicSalary = Double.parseDouble(employeeData[13].replace("\"", "").replace(",", ""));

                    hourlyRate = Double.parseDouble(employeeData[18]);

                    employeeFound = true;
                    break;
                }
            }
            employeeReader.close();

            // If employee not found, stop process
            if (!employeeFound) {
                System.out.println("Employee not found.");
                return;
            }

            // Read attendance once and store in memory (efficient)
            ArrayList<String[]> attendanceRecords = new ArrayList<>();
            BufferedReader attendanceReader = new BufferedReader(new FileReader(attendanceRecordFile));

            attendanceReader.readLine(); // skip header

            while ((line = attendanceReader.readLine()) != null) {
                attendanceRecords.add(line.split(",")); // simple split for attendance
            }
            attendanceReader.close();

            // Loop through months (June to December)
            for (int month = 6; month <= 12; month++) {

                // Initialize counters for each cutoff
                double firstCutoffHours = 0;
                double secondCutoffHours = 0;
                double firstCutoffLate = 0;
                double secondCutoffLate = 0;

                // Process each attendance record
                for (String[] attendanceData : attendanceRecords) {

                    // Skip if not matching employee ID
                    if (!attendanceData[0].trim().equals(employeeId.trim())) continue;

                    // Extract date parts (month/day/year)
                    String[] dateParts = attendanceData[3].split("/");
                    int monthNumber = Integer.parseInt(dateParts[0]);
                    int dayNumber = Integer.parseInt(dateParts[1]);

                    // Only process records for current month
                    if (monthNumber == month) {

                        // Compute daily worked hours and late hours
                        double[] workData = computeDailyWork(attendanceData[4], attendanceData[5]);

                        double dailyWorkedHours = workData[0];
                        double dailyLateHours = workData[1];

                        // Separate into first cutoff (1–15) and second cutoff (16–end)
                        if (dayNumber <= 15) {
                            firstCutoffHours += dailyWorkedHours;
                            firstCutoffLate += dailyLateHours;
                        } else {
                            secondCutoffHours += dailyWorkedHours;
                            secondCutoffLate += dailyLateHours;
                        }
                    }
                }

                // Only print report if there is data
                if (firstCutoffHours > 0 || secondCutoffHours > 0) {

                    printFinalReport(month, employeeName, employeeId,
                            firstCutoffHours, secondCutoffHours,
                            firstCutoffLate, secondCutoffLate,
                            hourlyRate, basicSalary);
                }
            }

        } catch (Exception e) {
            System.out.println("Error processing payroll.");
        }
    }

    public static double[] computeDailyWork(String timeIn, String timeOut) {

        // Split time into hours and minutes
        String[] timeInParts = timeIn.split(":");
        String[] timeOutParts = timeOut.split(":");

        // Convert time into decimal format (e.g., 8:30 → 8.5)
        double startTime = Integer.parseInt(timeInParts[0]) + Integer.parseInt(timeInParts[1]) / 60.0;
        double endTime = Integer.parseInt(timeOutParts[0]) + Integer.parseInt(timeOutParts[1]) / 60.0;

        // Clamp time to official working hours (8AM–5PM)
        startTime = Math.max(8.0, startTime);
        endTime = Math.min(17.0, endTime);

        // Compute late hours
        double lateHours = 0;

        int hour = Integer.parseInt(timeInParts[0]);
        int minute = Integer.parseInt(timeInParts[1]);

        // If employee arrives after 8:10 AM → considered late
        if (hour > 8 || (hour == 8 && minute >= 11)) {
            lateHours = 0.5; // fixed deduction
        }

        // Calculate total worked hours
        double workedHours = endTime - startTime;

        // Deduct 1 hour lunch break
        workedHours -= 1;

        return new double[]{
                Math.max(0, workedHours), // prevent negative hours
                lateHours
        };
    }

    public static void printFinalReport(int month, String employeeName, String employeeId,
                                       double firstCutoffHours, double secondCutoffHours,
                                       double firstCutoffLate, double secondCutoffLate,
                                       double hourlyRate, double basicSalary) {

        // Convert month number to name
        String monthName = getMonthName(month);

        // Compute salary per cutoff
        double firstCutoffSalary = firstCutoffHours * hourlyRate;
        double secondCutoffSalary = secondCutoffHours * hourlyRate;

        // Total salary before deductions
        double totalGrossSalary = firstCutoffSalary + secondCutoffSalary;

        // Government contributions
        double sssContribution = getSSS(basicSalary);
        double philHealthContribution = (basicSalary * 0.03) / 2;

        // Conditional computation for Pag-IBIG
        double pagIbigContribution = (basicSalary <= 1500)
                ? basicSalary * 0.01
                : basicSalary * 0.02;

        // Compute taxable income
        double taxableIncome = totalGrossSalary - (sssContribution + philHealthContribution + pagIbigContribution);

        // Compute tax
        double withholdingTax = getTax(taxableIncome);

        // Total deductions
        double totalDeductions = sssContribution + philHealthContribution + pagIbigContribution + withholdingTax;

        // Net salary for second cutoff
        double netSalarySecondCutoff = secondCutoffSalary - totalDeductions;

        // Display report
        System.out.println("\n========================================");
        System.out.println("Employee number: " + employeeId);
        System.out.println("Employee name: " + employeeName);

        System.out.println("\nCutoff date: 1 - 15 (" + monthName + ")");
        System.out.println("Total hours worked: " + firstCutoffHours);
        System.out.println("Late hours: " + firstCutoffLate);
        System.out.println("Gross salary: " + firstCutoffSalary);
        System.out.println("Net salary: " + firstCutoffSalary);

        System.out.println("\nCutoff date: 16 - End (" + monthName + ")");
        System.out.println("Total hours worked: " + secondCutoffHours);
        System.out.println("Late hours: " + secondCutoffLate);
        System.out.println("Gross salary: " + secondCutoffSalary);

        System.out.println("SSS: " + sssContribution);
        System.out.println("Phil-health: " + philHealthContribution);
        System.out.println("Pag-ibig: " + pagIbigContribution);
        System.out.println("Tax: " + withholdingTax);

        System.out.println("Total deductions: " + totalDeductions);
        System.out.println("Net salary: " + netSalarySecondCutoff);

        System.out.println("========================================");
    }

    // Converts month number to readable name
    public static String getMonthName(int month) {
        switch (month) {
            case 6: return "JUNE";
            case 7: return "JULY";
            case 8: return "AUGUST";
            case 9: return "SEPTEMBER";
            case 10: return "OCTOBER";
            case 11: return "NOVEMBER";
            case 12: return "DECEMBER";
        }
        return "";
    }

    public static double getSSS(double salary) {

        // Table of salary ranges and corresponding SSS contributions
        double[][] table = {
                {0, 3249, 135}, {3250, 3750, 157.5}, {3751, 4250, 180},
                {4251, 4750, 202.5}, {4751, 5250, 225}, {5251, 5750, 247.5},
                {5751, 6250, 270}, {6251, 6750, 292.5}, {6751, 7250, 315},
                {7251, 7750, 337.5}, {7751, 8250, 360}, {8251, 8750, 382.5},
                {8751, 9250, 405}, {9251, 9750, 427.5}, {9751, 10250, 450},
                {10251, 10750, 472.5}, {10751, 11250, 495}, {11251, 11750, 517.5},
                {11751, 12250, 540}, {12251, 12750, 562.5}, {12751, 13250, 585},
                {13251, 13750, 607.5}, {13751, 14250, 630}, {14251, 14750, 652.5},
                {14751, 15250, 675}, {15251, 15750, 697.5}, {15751, 16250, 720},
                {16251, 16750, 742.5}, {16751, 17250, 765}, {17251, 17750, 787.5},
                {17751, 18250, 810}, {18251, 18750, 832.5}, {18751, 19250, 855},
                {19251, 19750, 877.5}, {19751, 20250, 900}, {20251, 20750, 922.5},
                {20751, 21250, 945}, {21251, 21750, 967.5}, {21751, 22250, 990},
                {22251, 22750, 1012.5}, {22751, 23250, 1035}, {23251, 23750, 1057.5},
                {23751, 24250, 1080}, {24251, 24750, 1102.5}, {24751, Double.MAX_VALUE, 1125}
        };

        // Find matching salary range
        for (double[] row : table) {
            if (salary >= row[0] && salary <= row[1]) return row[2];
        }
        return 0;
    }

    public static double getTax(double income) {

        // Apply tax brackets
        if (income <= 20832) return 0;
        else if (income <= 33333) return (income - 20833) * 0.20;
        else if (income <= 66667) return 2500 + (income - 33333) * 0.25;
        else if (income <= 166667) return 10833 + (income - 66667) * 0.30;
        else if (income <= 666667) return 40833.33 + (income - 166667) * 0.32;
        else return 200833.33 + (income - 666667) * 0.35;
    }

    public static String[] manualSplit(String line) {

        // Array to store parsed CSV values
        String[] parsedValues = new String[19];

        String currentValue = "";
        int columnIndex = 0;

        // Tracks if we are inside quotation marks
        boolean insideQuotes = false;

        // Loop through each character in the line
        for (int i = 0; i < line.length(); i++) {

            char currentChar = line.charAt(i);

            // Toggle quote state
            if (currentChar == '\"') {
                insideQuotes = !insideQuotes;

            // If comma AND not inside quotes → split column
            } else if (currentChar == ',' && !insideQuotes) {
                parsedValues[columnIndex++] = currentValue;
                currentValue = "";

            } else {
                // Build the current value
                currentValue += currentChar;
            }
        }

        // Add last value
        parsedValues[columnIndex] = currentValue;

        return parsedValues;
    }
}
