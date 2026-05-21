/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.motorphpayroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class MotorPHPayroll {

    // =====================================================
    // FILE PATHS
    // =====================================================
    static String employeeDetailsFile =
            "src/main/java/com/mycompany/motorphpayroll/Employee Details.csv";

    static String attendanceRecordFile =
            "src/main/java/com/mycompany/motorphpayroll/Employee Attendance Record.csv";

    // =====================================================
    // WORK HOURS
    // =====================================================
    static final double START_WORK = 8.0;
    static final double END_WORK = 17.0;

    // =====================================================
    // FRAMES
    // =====================================================
    static JFrame loginFrame;
    static JFrame employeeFrame;
    static JFrame payrollFrame;

    // =====================================================
    // EMPLOYEE COMPONENTS
    // =====================================================
    static JTextField employeeNumberField;
    static JTextField employeeNameField;
    static JTextArea employeeOutputArea;

    // =====================================================
    // PAYROLL COMPONENTS
    // =====================================================
    static JTextField payrollEmployeeNumberField;
    static JTextField payrollEmployeeNameField;
    static JTextArea payrollOutputArea;

    // =====================================================
    // MAIN METHOD
    // =====================================================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                showLoginPage();
            }
        });
    }

    // =====================================================
    // LOGIN PAGE
    // =====================================================
    public static void showLoginPage() {

        loginFrame = new JFrame("MotorPH Login");

        loginFrame.setSize(550, 350);

        loginFrame.setLocationRelativeTo(null);

        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginFrame.setResizable(false);

        JPanel mainPanel =
                new JPanel(new BorderLayout(20, 20));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        JLabel titleLabel =
                new JLabel("MOTORPH PAYROLL SYSTEM");

        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel =
                new JPanel(new GridLayout(3, 2, 15, 15));

        JLabel usernameLabel =
                new JLabel("Username:");

        JTextField usernameField =
                new JTextField();

        JLabel passwordLabel =
                new JLabel("Password:");

        JPasswordField passwordField =
                new JPasswordField();

        JButton loginButton =
                new JButton("LOGIN");

        Font font =
                new Font("Arial", Font.PLAIN, 18);

        usernameLabel.setFont(font);
        passwordLabel.setFont(font);

        usernameField.setFont(font);
        passwordField.setFont(font);

        loginButton.setFont(font);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        formPanel.add(new JLabel(""));
        formPanel.add(loginButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // LOGIN BUTTON
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String username =
                        usernameField.getText().trim();

                String password =
                        new String(passwordField.getPassword());

                // EMPLOYEE LOGIN
                if (username.equals("employee")
                        && password.equals("12345")) {

                    loginFrame.dispose();

                    showEmployeePage();
                }

                // PAYROLL STAFF LOGIN
                else if (username.equals("payroll_staff")
                        && password.equals("12345")) {

                    loginFrame.dispose();

                    showPayrollPage();
                }

                // INVALID LOGIN
                else {

                    JOptionPane.showMessageDialog(
                            loginFrame,
                            "Please enter a valid username or password."
                    );
                }
            }
        });

        loginFrame.add(mainPanel);

        loginFrame.setVisible(true);
    }

    // =====================================================
    // EMPLOYEE PAGE
    // =====================================================
    public static void showEmployeePage() {

        employeeFrame =
                new JFrame("Employee Portal");

        employeeFrame.setSize(850, 650);

        employeeFrame.setLocationRelativeTo(null);

        employeeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        employeeFrame.setResizable(false);

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel titleLabel =
                new JLabel("EMPLOYEE PORTAL");

        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 26)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel =
                new JPanel(new GridLayout(4, 2, 15, 15));

        JLabel employeeNumberLabel =
                new JLabel("Employee Number:");

        employeeNumberField =
                new JTextField();

        JLabel employeeNameLabel =
                new JLabel("Employee Name:");

        employeeNameField =
                new JTextField();

        employeeNameField.setEditable(false);

        JButton viewButton =
                new JButton("View Employee");

        JButton backButton =
                new JButton("Back to main page");

        JButton exitButton =
                new JButton("Exit program");

        Font font =
                new Font("Arial", Font.PLAIN, 18);

        employeeNumberLabel.setFont(font);
        employeeNameLabel.setFont(font);

        employeeNumberField.setFont(font);
        employeeNameField.setFont(font);

        viewButton.setFont(font);
        backButton.setFont(font);
        exitButton.setFont(font);

        formPanel.add(employeeNumberLabel);
        formPanel.add(employeeNumberField);

        formPanel.add(employeeNameLabel);
        formPanel.add(employeeNameField);

        formPanel.add(viewButton);
        formPanel.add(backButton);

        formPanel.add(new JLabel(""));
        formPanel.add(exitButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        employeeOutputArea =
                new JTextArea();

        employeeOutputArea.setEditable(false);

        employeeOutputArea.setFont(
                new Font("Monospaced", Font.PLAIN, 16)
        );

        JScrollPane scrollPane =
                new JScrollPane(employeeOutputArea);

        scrollPane.setPreferredSize(
                new Dimension(780, 280)
        );

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // VIEW EMPLOYEE
        viewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                displayEmployeeInfo(
                        employeeNumberField
                                .getText()
                                .trim()
                );
            }
        });

        // BACK BUTTON
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                employeeFrame.dispose();

                showLoginPage();
            }
        });

        // EXIT BUTTON
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });

        employeeFrame.add(mainPanel);

        employeeFrame.setVisible(true);
    }

    // =====================================================
    // PAYROLL PAGE
    // =====================================================
    public static void showPayrollPage() {

        payrollFrame =
                new JFrame("Payroll Staff Portal");

        payrollFrame.setSize(1100, 780);

        payrollFrame.setLocationRelativeTo(null);

        payrollFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        payrollFrame.setResizable(false);

        JPanel mainPanel =
                new JPanel(new BorderLayout(20, 20));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel titleLabel =
                new JLabel("PAYROLL STAFF PORTAL");

        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel =
                new JPanel(new GridLayout(5, 2, 15, 15));

        JLabel employeeNumberLabel =
                new JLabel("Employee Number:");

        payrollEmployeeNumberField =
                new JTextField();

        JLabel employeeNameLabel =
                new JLabel("Employee Name:");

        payrollEmployeeNameField =
                new JTextField();

        payrollEmployeeNameField.setEditable(false);

        JButton processOneButton =
                new JButton("Process one employee");

        JButton processAllButton =
                new JButton("Process all employees");

        JButton backButton =
                new JButton("Back to main page");

        JButton exitButton =
                new JButton("Exit program");

        Font font =
                new Font("Arial", Font.PLAIN, 18);

        employeeNumberLabel.setFont(font);
        employeeNameLabel.setFont(font);

        payrollEmployeeNumberField.setFont(font);
        payrollEmployeeNameField.setFont(font);

        processOneButton.setFont(font);
        processAllButton.setFont(font);

        backButton.setFont(font);
        exitButton.setFont(font);

        formPanel.add(employeeNumberLabel);
        formPanel.add(payrollEmployeeNumberField);

        formPanel.add(employeeNameLabel);
        formPanel.add(payrollEmployeeNameField);

        formPanel.add(processOneButton);
        formPanel.add(processAllButton);

        formPanel.add(new JLabel(""));
        formPanel.add(backButton);

        formPanel.add(new JLabel(""));
        formPanel.add(exitButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        payrollOutputArea =
                new JTextArea();

        payrollOutputArea.setEditable(false);

        payrollOutputArea.setFont(
                new Font("Monospaced", Font.PLAIN, 15)
        );

        JScrollPane scrollPane =
                new JScrollPane(payrollOutputArea);

        scrollPane.setPreferredSize(
                new Dimension(1000, 450)
        );

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // PROCESS ONE EMPLOYEE
        processOneButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                payrollOutputArea.setText("");

                calculatePayroll(
                        payrollEmployeeNumberField
                                .getText()
                                .trim()
                );
            }
        });

        // PROCESS ALL EMPLOYEES
        processAllButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                payrollOutputArea.setText("");

                payrollEmployeeNumberField.setText("");

                payrollEmployeeNameField.setText("");

                for (int employeeId = 10001;
                     employeeId <= 10034;
                     employeeId++) {

                    calculatePayroll(
                            String.valueOf(employeeId)
                    );
                }
            }
        });

        // BACK BUTTON
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                payrollFrame.dispose();

                showLoginPage();
            }
        });

        // EXIT BUTTON
        exitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                System.exit(0);
            }
        });

        payrollFrame.add(mainPanel);

        payrollFrame.setVisible(true);
    }

    // =====================================================
    // DISPLAY EMPLOYEE INFO
    // =====================================================
    public static void displayEmployeeInfo(String employeeId) {

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(employeeDetailsFile)
                    );

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] employeeData =
                        manualSplit(line);

                if (employeeData[0]
                        .trim()
                        .equals(employeeId.trim())) {

                    String employeeName =
                            employeeData[2]
                                    + " "
                                    + employeeData[1];

                    employeeNameField.setText(employeeName);

                    employeeOutputArea.setText("");

                    employeeOutputArea.append(
                            "Employee Number : "
                                    + employeeData[0] + "\n"
                    );

                    employeeOutputArea.append(
                            "Employee Name : "
                                    + employeeName + "\n"
                    );

                    employeeOutputArea.append(
                            "Birthday : "
                                    + employeeData[3] + "\n"
                    );

                    return;
                }
            }

            employeeOutputArea.setText(
                    "Sorry. Employee number does not exist. Please type the correct one."
            );

        } catch (Exception e) {

            employeeOutputArea.setText(
                    "Error reading employee file."
            );
        }
    }

    // =====================================================
    // CALCULATE PAYROLL
    // =====================================================
    public static void calculatePayroll(String employeeId) {

        try {

            String employeeName = "";
            String employeeBirthday = "";

            double basicSalary = 0;
            double hourlyRate = 0;

            boolean employeeFound = false;

            BufferedReader employeeReader =
                    new BufferedReader(
                            new FileReader(employeeDetailsFile)
                    );

            String line;

            employeeReader.readLine();

            while ((line = employeeReader.readLine()) != null) {

                String[] employeeData =
                        manualSplit(line);

                if (employeeData[0]
                        .trim()
                        .equals(employeeId.trim())) {

                    employeeName =
                            employeeData[2]
                                    + " "
                                    + employeeData[1];

                    employeeBirthday =
                            employeeData[3];

                    basicSalary =
                            Double.parseDouble(
                                    employeeData[13]
                                            .replace("\"", "")
                                            .replace(",", "")
                            );

                    hourlyRate =
                            Double.parseDouble(
                                    employeeData[18]
                            );

                    employeeFound = true;

                    if (!payrollEmployeeNumberField
                            .getText()
                            .isEmpty()) {

                        payrollEmployeeNameField
                                .setText(employeeName);
                    }

                    break;
                }
            }

            employeeReader.close();

            if (!employeeFound) {

                payrollOutputArea.append(
                        "\nSorry. Employee number does not exist. Please type the correct one.\n"
                );

                return;
            }

            ArrayList<String[]> attendanceRecords =
                    new ArrayList<>();

            BufferedReader attendanceReader =
                    new BufferedReader(
                            new FileReader(
                                    attendanceRecordFile
                            )
                    );

            attendanceReader.readLine();

            while ((line = attendanceReader.readLine()) != null) {

                attendanceRecords.add(
                        line.split(",")
                );
            }

            attendanceReader.close();

            boolean hasPayroll = false;

            for (int month = 1;
                 month <= 12;
                 month++) {

                double firstCutoffHours = 0;
                double secondCutoffHours = 0;

                for (String[] attendanceData
                        : attendanceRecords) {

                    if (!attendanceData[0]
                            .trim()
                            .equals(employeeId.trim())) {

                        continue;
                    }

                    String[] dateParts =
                            attendanceData[3]
                                    .split("/");

                    int monthNumber =
                            Integer.parseInt(dateParts[0]);

                    int dayNumber =
                            Integer.parseInt(dateParts[1]);

                    if (monthNumber == month) {

                        double[] workData =
                                computeDailyWork(
                                        attendanceData[4],
                                        attendanceData[5]
                                );

                        double workedHours =
                                workData[0];

                        if (dayNumber <= 15) {

                            firstCutoffHours += workedHours;
                        }

                        else {

                            secondCutoffHours += workedHours;
                        }
                    }
                }

                if (firstCutoffHours > 0
                        || secondCutoffHours > 0) {

                    hasPayroll = true;

                    String monthName =
                            getMonthName(month);

                    double firstSalary =
                            firstCutoffHours * hourlyRate;

                    double secondSalary =
                            secondCutoffHours * hourlyRate;

                    double totalGross =
                            firstSalary + secondSalary;

                    double sss =
                            getSSS(basicSalary);

                    double philHealth =
                            (basicSalary * 0.03) / 2;

                    double pagibig =
                            basicSalary * 0.02;

                    double taxableIncome =
                            totalGross
                                    - (sss + philHealth + pagibig);

                    double tax =
                            getTax(taxableIncome);

                    double deductions =
                            sss + philHealth + pagibig + tax;

                    double netSalary =
                            totalGross - deductions;

                    // =====================================================
                    // PAYROLL REPORT
                    // =====================================================
                    payrollOutputArea.append(
                            "\n====================================================\n"
                    );

                    payrollOutputArea.append(
                            "MONTH : "
                                    + monthName + "\n"
                    );

                    payrollOutputArea.append(
                            "====================================================\n"
                    );

                    payrollOutputArea.append(
                            "Employee Number : "
                                    + employeeId + "\n"
                    );

                    payrollOutputArea.append(
                            "Employee Name : "
                                    + employeeName + "\n"
                    );

                    payrollOutputArea.append(
                            "Birthday : "
                                    + employeeBirthday + "\n"
                    );

                    payrollOutputArea.append(
                            "\nFIRST CUT OFF\n"
                    );

                    payrollOutputArea.append(
                            "Coverage : "
                                    + monthName
                                    + " 1 - 15\n"
                    );

                    payrollOutputArea.append(
                            "Hours Worked : "
                                    + String.format(
                                    "%.2f",
                                    firstCutoffHours
                            ) + "\n"
                    );

                    payrollOutputArea.append(
                            "Gross Salary : "
                                    + String.format(
                                    "%.2f",
                                    firstSalary
                            ) + "\n"
                    );

                    payrollOutputArea.append(
                            "Net Salary : "
                                    + String.format(
                                    "%.2f",
                                    firstSalary
                            ) + "\n"
                    );

                    payrollOutputArea.append(
                            "\nSECOND CUT OFF\n"
                    );

                    payrollOutputArea.append(
                            "Coverage : "
                                    + monthName
                                    + " 16 - END\n"
                    );

                    payrollOutputArea.append(
                            "Hours Worked : "
                                    + String.format(
                                    "%.2f",
                                    secondCutoffHours
                            ) + "\n"
                    );

                    payrollOutputArea.append(
                            "Gross Salary : "
                                    + String.format(
                                    "%.2f",
                                    secondSalary
                            ) + "\n"
                    );

                    payrollOutputArea.append(
                            "\nDEDUCTIONS\n"
                    );

                    payrollOutputArea.append(
                            "SSS : "
                                    + String.format("%.2f", sss)
                                    + "\n"
                    );

                    payrollOutputArea.append(
                            "PhilHealth : "
                                    + String.format("%.2f", philHealth)
                                    + "\n"
                    );

                    payrollOutputArea.append(
                            "Pag-IBIG : "
                                    + String.format("%.2f", pagibig)
                                    + "\n"
                    );

                    payrollOutputArea.append(
                            "Tax : "
                                    + String.format("%.2f", tax)
                                    + "\n"
                    );

                    payrollOutputArea.append(
                            "\nTotal Deductions : "
                                    + String.format("%.2f", deductions)
                                    + "\n"
                    );

                    payrollOutputArea.append(
                            "Net Salary : "
                                    + String.format("%.2f", netSalary)
                                    + "\n"
                    );

                    payrollOutputArea.append(
                            "====================================================\n"
                    );
                }
            }

            if (!hasPayroll) {

                payrollOutputArea.append(
                        "\nNo payroll records found.\n"
                );
            }

        } catch (Exception e) {

            payrollOutputArea.append(
                    "\nError processing payroll.\n"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // COMPUTE DAILY WORK
    // =====================================================
    public static double[] computeDailyWork(
            String timeIn,
            String timeOut
    ) {

        String[] timeInParts =
                timeIn.split(":");

        String[] timeOutParts =
                timeOut.split(":");

        double startTime =
                Integer.parseInt(timeInParts[0])
                        + Integer.parseInt(timeInParts[1]) / 60.0;

        double endTime =
                Integer.parseInt(timeOutParts[0])
                        + Integer.parseInt(timeOutParts[1]) / 60.0;

        startTime =
                Math.max(START_WORK, startTime);

        endTime =
                Math.min(END_WORK, endTime);

        double workedHours =
                endTime - startTime;

        workedHours -= 1;

        return new double[]{
                Math.max(0, workedHours)
        };
    }

    // =====================================================
    // MONTH NAME
    // =====================================================
    public static String getMonthName(int month) {

        switch (month) {

            case 1:
                return "JANUARY";

            case 2:
                return "FEBRUARY";

            case 3:
                return "MARCH";

            case 4:
                return "APRIL";

            case 5:
                return "MAY";

            case 6:
                return "JUNE";

            case 7:
                return "JULY";

            case 8:
                return "AUGUST";

            case 9:
                return "SEPTEMBER";

            case 10:
                return "OCTOBER";

            case 11:
                return "NOVEMBER";

            case 12:
                return "DECEMBER";
        }

        return "";
    }

    // =====================================================
    // SSS
    // =====================================================
    public static double getSSS(double salary) {

        if (salary <= 3250)
            return 135;

        else if (salary <= 3750)
            return 157.5;

        else if (salary <= 4250)
            return 180;

        else
            return 1125;
    }

    // =====================================================
    // TAX
    // =====================================================
    public static double getTax(double income) {

        if (income <= 20832)
            return 0;

        else if (income <= 33333)
            return (income - 20833) * 0.20;

        else if (income <= 66667)
            return 2500 + (income - 33333) * 0.25;

        else
            return 0;
    }

    // =====================================================
    // CSV SPLIT
    // =====================================================
    public static String[] manualSplit(String line) {

        String[] parsedValues =
                new String[19];

        String currentValue = "";

        int columnIndex = 0;

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char currentChar =
                    line.charAt(i);

            if (currentChar == '\"') {

                insideQuotes = !insideQuotes;
            }

            else if (
                    currentChar == ','
                            && !insideQuotes
            ) {

                parsedValues[columnIndex++] =
                        currentValue;

                currentValue = "";
            }

            else {

                currentValue += currentChar;
            }
        }

        parsedValues[columnIndex] =
                currentValue;

        return parsedValues;
    }
}