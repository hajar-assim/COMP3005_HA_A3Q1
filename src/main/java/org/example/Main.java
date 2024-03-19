package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5433/a3demo";
        String user = "postgres";
        String password = "postgres";

        try{
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null){
                System.out.println("Connected to database successfully.\n");

                Scanner scanner = new Scanner(System.in);
                boolean exit = false;

                while (!exit) {
                    System.out.println("\n-------------------------");
                    System.out.println("Choose an option:");
                    System.out.println("1. Retrieve all students");
                    System.out.println("2. Add a new student");
                    System.out.println("3. Update a student's email");
                    System.out.println("4. Delete a student");
                    System.out.println("5. Exit");
                    System.out.println("-------------------------\n");

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            getAllStudents(connection);
                            break;
                        case 2:
                            System.out.println("Enter first name:");
                            String firstName = scanner.nextLine();
                            System.out.println("Enter last name:");
                            String lastName = scanner.nextLine();
                            System.out.println("Enter email:");
                            String email = scanner.nextLine();
                            System.out.println("Enter enrollment date (YYYY-MM-DD):");
                            String enrollmentDate = scanner.nextLine();
                            addStudent(connection, firstName, lastName, email, enrollmentDate);
                            break;
                        case 3:
                            System.out.println("Enter student ID:");
                            int studentIdToUpdate = scanner.nextInt();
                            scanner.nextLine();
                            System.out.println("Enter new email:");
                            String newEmail = scanner.nextLine();
                            updateStudentEmail(connection, studentIdToUpdate, newEmail);
                            break;
                        case 4:
                            System.out.println("Enter student ID to delete:");
                            int studentIdToDelete = scanner.nextInt();
                            deleteStudent(connection, studentIdToDelete);
                            break;
                        case 5:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }

            } else {
                System.out.println("Failed to connect to database");
            }

        } catch(Exception e) {
            System.out.println("Caught an exception: " + e);
        }

    }

    /**
     * Retrieves all students from the database.
     *
     * @param connection The database connection.
     * @throws SQLException If a database access error occurs.
     */
    private static void getAllStudents(Connection connection) throws SQLException {
        String query = "SELECT * FROM students";

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String enrollmentDate = resultSet.getString("enrollment_date");

                System.out.println("student_id: " + studentId +
                        ", first_name: " + firstName +
                        ", last_name: " + lastName +
                        ", email: " + email +
                        ", enrollment_date: " + enrollmentDate);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Inserts a new student record into the students table.
     *
     * @param connection The database connection.
     * @param firstName The student's first name.
     * @param lastName The student's last name.
     * @param email The student's email address.
     * @param enrollmentDate The enrollment date of the student (in format: "yyyy-MM-dd").
     * @throws SQLException If a database access error occurs.
     */
    public static void addStudent(Connection connection, String firstName, String lastName, String email, String enrollmentDate) throws SQLException {
        String query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, enrollmentDate);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student added successfully.");
            } else {
                System.out.println("Failed to add student.");
            }
        }
    }

    /**
     * Updates the email address for a student with the specified student_id.
     *
     * @param connection The database connection.
     * @param studentId The ID of the student whose email address will be updated.
     * @param newEmail The new email address for the student.
     * @throws SQLException If a database access error occurs.
     */
    public static void updateStudentEmail(Connection connection, int studentId, String newEmail) throws SQLException {
        String query = "UPDATE students SET email = '" + newEmail + "' WHERE student_id = " + studentId;
        try {
            int rowsAffected = connection.createStatement().executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Email address updated successfully for student with ID: " + studentId);
            } else {
                System.out.println("Failed to update email address for student with ID: " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating email address: " + e.getMessage());
        }
    }

    /**
     * Deletes the record of the student with the specified student_id.
     *
     * @param connection The database connection.
     * @param studentId The ID of the student to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public static void deleteStudent(Connection connection, int studentId) throws SQLException {
        String query = "DELETE FROM students WHERE student_id = " + studentId;
        try {
            int rowsAffected = connection.createStatement().executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Student with ID " + studentId + " deleted successfully.");
            } else {
                System.out.println("Failed to delete student with ID " + studentId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

}