# MO-IT101-Group16 | MotorPH Payroll System
Basic presentation of employee details (employee number, employee name & birthday) and automatic salary calculation based on the number of hours worked and basic deductions.
## Group 16 Members
Jeff Trinidad (lr.jeftrinidad@mmdc.mcl.edu.ph)

Patricia Kaye Red (lr.pkred@mmdc.mcl.edu.ph)

Aileen Rodriguez (lr.arodriguez@mmdc.mcl.edu.ph)

Alexandra Madria (lr.amadria@mmdc.mcl.edu.ph)

Alea Valeriano (lr.avaleriano@mmdc.mcl.edu.ph)

-File reader and scanner.

import java.io.BufferedReader; // Sits on top of the FileReader to store the data and let you read it line-by-line.


import java.io.FileReader; // Connects to the physical file on the disk.


import java.util.Scanner; // Stays active in the background, waiting for you to tell it what the user wants to do next.

-Reference file on the program (CSV) for employee details and attendance.

static String detailsFile = "src/main/java/com/mycompany/motorphpayroll/Employee Details.csv"; // File for the employee details


static String attendanceFile = "src/main/java/com/mycompany/motorphpayroll/Employee Attendance Record.csv"; // File for the attendance

-This code checks the login to show menus for either basic information or payroll processing. (line 22-55)

if (user.equals("employee") && pass.equals("12345")) {
System.out.println("\n1. Enter your employee number\n2. Exit program");
System.out.print("Choice: ");
int choice = scan.nextInt();
if (choice == 1) {
System.out.print("Enter ID: ");
displayEmployeeInfo(scan.next());
}
} else if (user.equals("payroll staff") && pass.equals("12345")) {
System.out.println("\n1. Process Payroll\n2. Exit");
System.out.print("Choice: ");
int menu = scan.nextInt();
if (menu == 1) {
System.out.println("\n1. Process One Employee\n2. Process All Employees");
System.out.print("Choice: ");
int subMenu = scan.nextInt();
if (subMenu == 1) {
System.out.print("Enter Employee ID: ");
calculatePayroll(scan.next());
} else if (subMenu == 2) {
for (int i = 10001; i <= 10025; i++) {
calculatePayroll(String.valueOf(i));
}
}
}
} else {
System.out.println("Invalid Login.");
}

-It reads the file line-by-line to find and display matching employee details by employee number.

public static void displayEmployeeInfo(String empId) {
try (BufferedReader br = new BufferedReader(new FileReader(detailsFile))) {
String line;
br.readLine();
while ((line = br.readLine()) != null) {
String[] d = manualSplit(line); // We use the manualSplit, because we encounter errors on using the .split(",").
if (d[0].trim().equals(empId.trim())) {
System.out.println("\n--- Employee Details ---");
System.out.println("a. Employee number: " + d[0]);
System.out.println("b. Employee name: " + d[2] + " " + d[1]);
System.out.println("c. Birthday: " + d[3]);
return;
}
}
System.out.println("\na. employee number does not exist.");
} catch (Exception e) { System.out.println("Error reading file."); }
}

-Finds employee salary, sums monthly attendance hours and triggers the final payroll report calculation.(line 77-129)

public static void calculatePayroll(String empId) {
try {
String name = "";
double basic = 0, rate = 0;
boolean found = false;
BufferedReader br1 = new BufferedReader(new FileReader(detailsFile));
String line = br1.readLine();
while ((line = br1.readLine()) != null) {
String[] d = manualSplit(line);
if (d[0].trim().equals(empId.trim())) {
name = d[2] + " " + d[1];
basic = Double.parseDouble(d[13].replace(""", "").replace(",", ""));
rate = Double.parseDouble(d[18]);
found = true;
break;
}
}
br1.close();
if (!found) return;
for (int m = 6; m <= 12; m++) {
double h1 = 0, h2 = 0, l1 = 0, l2 = 0;
BufferedReader br2 = new BufferedReader(new FileReader(attendanceFile));
br2.readLine();
while ((line = br2.readLine()) != null) {
String[] att = line.split(",");
if (att[0].trim().equals(empId.trim())) {
String[] date = att[3].split("/");
int monthNum = Integer.parseInt(date[0]);
int dayNum = Integer.parseInt(date[1]);
if (monthNum == m) {
double dailyHours = 8.0;
double latePenalty = checkLate(att[4]);
if (dayNum <= 15) {
h1 += dailyHours;
l1 += latePenalty;
} else {
h2 += dailyHours;
l2 += latePenalty;
}
}
}
}
br2.close();
if (h1 > 0 || h2 > 0) printFinalReport(m, name, empId, h1, h2, l1, l2, rate, basic);
}
} catch (Exception e) { System.out.println("Error processing payroll for ID: " + empId); }
}

-Splits CSV lines into columns while correctly ignoring commas found inside quotation marks.(line 131-151)

public static String[] manualSplit(String line) {
String[] result = new String[19];
String currentPart = "";
int colIndex = 0;
boolean insideQuotes = false;
for (int i = 0; i < line.length(); i++) {
char c = line.charAt(i);
if (c == '"') {
insideQuotes = !insideQuotes;
} else if (c == ',' && !insideQuotes) {
if (colIndex < result.length) result[colIndex] = currentPart;
currentPart = "";
colIndex++;
} else {
currentPart += c;
}
}
if (colIndex < result.length) result[colIndex] = currentPart;
return result;
}

-Checks login time and returns a half-hour penalty if logging in after 8:10 AM. (line 153-158)

public static double checkLate(String time) {
String[] t = time.split(":");
int h = Integer.parseInt(t[0]);
int min = Integer.parseInt(t[1]);
return (h > 8 || (h == 8 && min >= 11)) ? 0.5 : 0.0;
}

-Calculates net pay by subtracting late penalties and mandatory government deductions from gross earnings.(line 161-185)

public static void printFinalReport(int m, String name, String id, double h1, double h2, double l1, double l2, double rate, double basic) {
String monthName = "";
switch(m) {
case 6: monthName = "JUNE"; break;
case 7: monthName = "JULY"; break;
case 8: monthName = "AUGUST"; break;
case 9: monthName = "SEPTEMBER"; break;
case 10: monthName = "OCTOBER"; break;
case 11: monthName = "NOVEMBER"; break;
case 12: monthName = "DECEMBER"; break;
}
double sss = getSSS(basic);
double ph = (basic * 0.03) / 2;
double pi = (basic > 1500) ? 100 : (basic * 0.01);
double tax = getTax(basic - (sss + ph + pi));
double netHours1 = h1 - l1;
double netHours2 = h2 - l2;
System.out.println("\nPAYROLL FOR " + monthName + " | " + name + " (" + id + ")");
System.out.println("[1ST CUT-OFF] Total Hours: " + h1 + " | Late: " + l1 + " | Net Payout: " + (netHours1 * rate));
System.out.println("[2ND CUT-OFF] Total Hours: " + h2 + " | Late: " + l2 + " | Deductions: SSS=" + sss + ", Phil-health=" + ph + ", Pag-ibig=" + pi + ", Tax=" + tax);
System.out.println(" Net Payout: " + ((netHours2 * rate) - (sss + ph + pi + tax)));
}

-Returns the specific SSS contribution amount based on which salary bracket the income falls into.(line 188-233)

public static double getSSS(double s) {
if (s < 3250) return 135.00;
else if (s >= 3250 && s <= 3750) return 157.50;
else if (s >= 3751 && s <= 4250) return 180.00;
else if (s >= 4251 && s <= 4750) return 202.50;
else if (s >= 4751 && s <= 5250) return 225.00;
else if (s >= 5251 && s <= 5750) return 247.50;
else if (s >= 5751 && s <= 6250) return 270.00;
else if (s >= 6251 && s <= 6750) return 292.50;
else if (s >= 6751 && s <= 7250) return 315.00;
else if (s >= 7251 && s <= 7750) return 337.50;
else if (s >= 7751 && s <= 8250) return 360.00;
else if (s >= 8251 && s <= 8750) return 382.50;
else if (s >= 8751 && s <= 9250) return 405.00;
else if (s >= 9251 && s <= 9750) return 427.50;
else if (s >= 9751 && s <= 10250) return 450.00;
else if (s >= 10251 && s <= 10750) return 472.50;
else if (s >= 10751 && s <= 11250) return 495.00;
else if (s >= 11251 && s <= 11750) return 517.50;
else if (s >= 11751 && s <= 12250) return 540.00;
else if (s >= 12251 && s <= 12750) return 562.50;
else if (s >= 12751 && s <= 13250) return 585.00;
else if (s >= 13251 && s <= 13750) return 607.50;
else if (s >= 13751 && s <= 14250) return 630.00;
else if (s >= 14251 && s <= 14750) return 652.50;
else if (s >= 14751 && s <= 15250) return 675.00;
else if (s >= 15251 && s <= 15750) return 697.50;
else if (s >= 15751 && s <= 16250) return 720.00;
else if (s >= 16251 && s <= 16750) return 742.50;
else if (s >= 16751 && s <= 17250) return 765.00;
else if (s >= 17251 && s <= 17750) return 787.50;
else if (s >= 17751 && s <= 18250) return 810.00;
else if (s >= 18251 && s <= 18750) return 832.50;
else if (s >= 18751 && s <= 19250) return 855.00;
else if (s >= 19251 && s <= 19750) return 877.50;
else if (s >= 19751 && s <= 20250) return 900.00;
else if (s >= 20251 && s <= 20750) return 922.50;
else if (s >= 20751 && s <= 21250) return 945.00;
else if (s >= 21251 && s <= 21750) return 967.50;
else if (s >= 21751 && s <= 22250) return 990.00;
else if (s >= 22251 && s <= 22750) return 1012.50;
else if (s >= 22751 && s <= 23250) return 1035.00;
else if (s >= 23251 && s <= 23750) return 1057.50;
else if (s >= 23751 && s <= 24250) return 1080.00;
else if (s >= 24251 && s <= 24750) return 1102.50;
else return 1125.00;
}

-Calculates monthly withholding tax based on the tax brackets and percentage rates.

public static double getTax(double income) {
if (income <= 20832) return 0;
else if (income <= 33333) return (income - 20833) * 0.20;
else if (income <= 66667) return 2500 + (income - 33333) * 0.25;
else if (income <= 166667) return 10833 + (income - 66667) * 0.30;
else if (income <= 666667) return 40833.33 + (income - 166667) * 0.32;
else return 200833.33 + (income - 666667) * 0.35;
}
