import com.onlinevotingsystem.db.*;
import com.onlinevotingsystem.util.InputValidator;
import com.onlinevotingsystem.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class VoterLoginPortal {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Initialize the database (create tables)
            DBInitializer.initializeDatabase();

            connection = DBConnection.getConnection();
            Logger.log("Voter Login Portal Started");

            // Get voter login details
            String voterId = getVoterId();
            String dateOfBirth = getDateOfBirth();

            // Validate voter login
            if (validateVoterLogin(connection, voterId, dateOfBirth)) {
                System.out.println("Login successful. Welcome, voter!");
            } else {
                System.out.println("Login failed. Invalid voter ID, date of birth, or security answer.");
            }

            Logger.log("Voter Login Portal Completed");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle database connection exceptions
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getVoterId() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your voter ID: ");
        return scanner.nextLine();
    }

    private static String getDateOfBirth() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your date of birth (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    private static boolean validateVoterLogin(Connection connection, String voterId, String dateOfBirth) throws SQLException {
        // Validate the voter login using voter ID and date of birth
        if (InputValidator.isNullOrEmpty(voterId) || InputValidator.isNullOrEmpty(dateOfBirth)) {
            return false; // Invalid input
        }

        // Check if the voter exists
        String checkVoterQuery = "SELECT * FROM voters WHERE voter_id = ? AND date_of_birth = ?";
        try (PreparedStatement checkVoterStatement = connection.prepareStatement(checkVoterQuery)) {
            checkVoterStatement.setString(1, voterId);
            checkVoterStatement.setString(2, dateOfBirth);
            try (ResultSet checkVoterResult = checkVoterStatement.executeQuery()) {
                if (checkVoterResult.next()) {
                    // Voter exists, now get and display the security question
                    String securityQuestion = checkVoterResult.getString("security_question");
                    System.out.println("Security Question: " + securityQuestion);

                    // Get security answer
                    System.out.print("Enter your security answer: ");
                    String securityAnswer = new Scanner(System.in).nextLine();

                    // Validate security answer
                    String validateSecurityAnswerQuery = "SELECT * FROM voters WHERE voter_id = ? AND security_answer = ?";
                    try (PreparedStatement validateSecurityAnswerStatement = connection.prepareStatement(validateSecurityAnswerQuery)) {
                        validateSecurityAnswerStatement.setString(1, voterId);
                        validateSecurityAnswerStatement.setString(2, securityAnswer);
                        try (ResultSet validateSecurityAnswerResult = validateSecurityAnswerStatement.executeQuery()) {
                            return validateSecurityAnswerResult.next(); // Returns true if the security answer is valid
                        }
                    }
                }
            }
        }

        return false; // Voter does not exist
    }
}
