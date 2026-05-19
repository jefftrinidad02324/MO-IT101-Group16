package com.mycompany.motorphpayroll;

import javax.swing.*;
import java.awt.*;

public class MotorPHPayroll {

    static JFrame frame;
    static CardLayout cardLayout;
    static JPanel mainPanel;

    // Login
    static JTextField usernameField;
    static JPasswordField passwordField;

    // Employee
    static JTextField employeeNumberField;
    static JTextField employeeNameField;
    static JTextArea employeeOutputArea;

    // Payroll
    static JTextField payrollEmployeeNumberField;
    static JTextField payrollEmployeeNameField;
    static JTextArea payrollOutputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MotorPHPayroll::createUI);
    }

    // ================= MAIN UI =================
    public static void createUI() {

        frame = new JFrame("MotorPH System");
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "LOGIN");
        mainPanel.add(employeePanel(), "EMPLOYEE");
        mainPanel.add(payrollPanel(), "PAYROLL");

        // ================= OUTER PADDING WRAPPER =================
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        wrapper.add(mainPanel, BorderLayout.CENTER);

        frame.add(wrapper);
        frame.setVisible(true);

        cardLayout.show(mainPanel, "LOGIN");
    }

    // ================= LOGIN =================
    public static JPanel loginPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 10, 8, 10);

        usernameField = new JTextField(16);
        passwordField = new JPasswordField(16);

        JButton login = new JButton("LOGIN");
        JButton exit = new JButton("EXIT PROGRAM");

        // ================= TITLE =================
        JLabel title = new JLabel("MotorPH Payroll System");
        title.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(login);
        buttonPanel.add(exit);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        login.addActionListener(e -> {

            String u = usernameField.getText();
            String p = new String(passwordField.getPassword());

            if (u.equals("employee") && p.equals("12345")) {
                cardLayout.show(mainPanel, "EMPLOYEE");
            } else if (u.equals("payroll_staff") && p.equals("12345")) {
                cardLayout.show(mainPanel, "PAYROLL");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid login");
            }
        });

        exit.addActionListener(e -> System.exit(0));

        return panel;
    }

    // ================= EMPLOYEE =================
    public static JPanel employeePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(3, 2, 10, 10));

        employeeNumberField = new JTextField();
        employeeNameField = new JTextField();
        employeeNameField.setEditable(false);

        JButton view = new JButton("View");
        JButton back = new JButton("Back");
        JButton exit = new JButton("Exit Program");

        top.add(new JLabel("Employee No"));
        top.add(employeeNumberField);
        top.add(new JLabel("Name"));
        top.add(employeeNameField);
        top.add(view);
        top.add(back);

        employeeOutputArea = new JTextArea();
        employeeOutputArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(employeeOutputArea);

        JPanel bottom = new JPanel();
        bottom.add(exit);

        view.addActionListener(e -> {

            String id = employeeNumberField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter Employee Number");
                return;
            }

            EmployeeService.displayEmployee(
                    id,
                    employeeNameField,
                    employeeOutputArea
            );
        });

        back.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        exit.addActionListener(e -> System.exit(0));

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ================= PAYROLL =================
    public static JPanel payrollPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(4, 2, 10, 10));

        payrollEmployeeNumberField = new JTextField();
        payrollEmployeeNameField = new JTextField();
        payrollEmployeeNameField.setEditable(false);

        JButton one = new JButton("Process One");
        JButton all = new JButton("Process All");
        JButton back = new JButton("Back");
        JButton exit = new JButton("Exit Program");

        top.add(new JLabel("Employee No"));
        top.add(payrollEmployeeNumberField);
        top.add(new JLabel("Name"));
        top.add(payrollEmployeeNameField);
        top.add(one);
        top.add(all);
        top.add(back);
        top.add(exit);

        payrollOutputArea = new JTextArea();
        payrollOutputArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(payrollOutputArea);

        one.addActionListener(e -> {

            String id = payrollEmployeeNumberField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter Employee Number");
                return;
            }

            PayrollService.processOne(
                    id,
                    payrollEmployeeNameField,
                    payrollOutputArea
            );
        });

        all.addActionListener(e ->
                PayrollService.processAll(payrollOutputArea)
        );

        back.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        exit.addActionListener(e -> System.exit(0));

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }
}