package com.mycompany.motorphpayroll;

import javax.swing.*;
import java.io.*;

public class EmployeeService {

    static String file =
            "src/main/java/com/mycompany/motorphpayroll/Employee Details.csv";

    public static void displayEmployee(String id, JTextField nameField, JTextArea output) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {

                // FIX: replaced broken manualSplit
                String[] d = line.split(",", -1);

                if (d.length < 4) continue;

                if (d[0].trim().equals(id.trim())) {

                    String name = d[2] + " " + d[1];
                    nameField.setText(name);

                    output.setText(
                            "Employee ID: " + d[0] + "\n" +
                            "Name: " + name + "\n" +
                            "Birthday: " + d[3]
                    );
                    return;
                }
            }

            output.setText("Employee not found.");

        } catch (Exception e) {
            output.setText("Error reading file: " + e.getMessage());
        }
    }
}