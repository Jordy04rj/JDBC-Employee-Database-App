package JdbcEmployeeDatabaseApp;

import java.sql.*;
import java.util.Scanner;

public class EmployeeDBApp {

    static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    static final String DB_USER = "root";
    static final String DB_PASS = "root"; // Change as per your setup

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Employee Database ---");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> addEmployee(scanner);
                case 2 -> viewEmployees();
                case 3 -> updateEmployee(scanner);
                case 4 -> deleteEmployee(scanner);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private static void addEmployee(Scanner scanner) {
        try (Connection conn = connect()) {
            String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            System.out.print("Enter Name: ");
            scanner.nextLine(); // consume newline
            String name = scanner.nextLine();
            System.out.print("Enter Department: ");
            String dept = scanner.nextLine();
            System.out.print("Enter Salary: ");
            double salary = scanner.nextDouble();

            stmt.setString(1, name);
            stmt.setString(2, dept);
            stmt.setDouble(3, salary);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " employee(s) added.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewEmployees() {
        try (Connection conn = connect()) {
            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\nID | Name | Department | Salary");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateEmployee(Scanner scanner) {
        try (Connection conn = connect()) {
            String sql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            System.out.print("Enter Employee ID to Update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Department: ");
            String dept = scanner.nextLine();
            System.out.print("Enter New Salary: ");
            double salary = scanner.nextDouble();

            stmt.setString(1, name);
            stmt.setString(2, dept);
            stmt.setDouble(3, salary);
            stmt.setInt(4, id);

            int rows = stmt.executeUpdate();
            System.out.println(rows + " employee(s) updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteEmployee(Scanner scanner) {
        try (Connection conn = connect()) {
            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            System.out.print("Enter Employee ID to Delete: ");
            int id = scanner.nextInt();

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows + " employee(s) deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
