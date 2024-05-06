import com.onlinevotingsystem.candidate.Candidate;
import com.onlinevotingsystem.poll.Poll;
import com.onlinevotingsystem.registration.Voter;
import com.onlinevotingsystem.result.Result;
import com.onlinevotingsystem.db.DBConnection;
import com.onlinevotingsystem.db.DBInitializer;
import com.onlinevotingsystem.util.InputValidator;
import com.onlinevotingsystem.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class OnlineVotingSystemApp {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Initialize the database (create tables)
            DBInitializer.initializeDatabase();

            connection = DBConnection.getConnection();
            Logger.log("Online Voting System Application Started");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Welcome to the Online Voting System");
                System.out.println("1. Voter Registration");
                System.out.println("2. Candidate Registration");
                System.out.println("3. Create a Poll");
                System.out.println("4. Record Election Results");
                System.out.println("5. Exit");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        // Voter Registration
                        System.out.print("Enter voter's name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter voter's email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter voter's ID: ");
                        String voterID = scanner.nextLine();

                        System.out.print("Enter voter's address: ");
                        String address = scanner.nextLine();

                        System.out.print("Enter voter's date of birth: ");
                        String dateOfBirth = scanner.nextLine();

                        System.out.print("Enter security question: ");
                        String securityQuestion = scanner.nextLine();

                        System.out.print("Enter security answer: ");
                        String securityAnswer = scanner.nextLine();

                        if (InputValidator.isEmailValid(email)) {
                            Voter voter = new Voter(name, email, voterID, address, dateOfBirth,
                                    securityQuestion, securityAnswer);

                            String insertQuery = "INSERT INTO voters (name, email, voter_id, address, " +
                                    "date_of_birth, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";

                            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

                            preparedStatement.setString(1, name);
                            preparedStatement.setString(2, email);
                            preparedStatement.setString(3, voterID);
                            preparedStatement.setString(4, address);
                            preparedStatement.setString(5, dateOfBirth);
                            preparedStatement.setString(6, securityQuestion);
                            preparedStatement.setString(7, securityAnswer);

                            int rowsAffected = preparedStatement.executeUpdate();

                            if (rowsAffected > 0) {
                                System.out.println("Data inserted successfully.");
                            } else {
                                System.out.println("Data insertion failed.");
                            }
                            Logger.log("Registered voter: " + voter.getName());
                            Logger.log("Voter mail: " + email);
                            Logger.log("Voter ID: " + voterID);
                            Logger.log("Date of birth: " + dateOfBirth);

                        } else {
                            System.out.println("Invalid email address. Registration failed.");
                        }

                        break;

                    case 2:
                        // Candidate Registration
                        System.out.print("Enter candidate's name: ");
                        String candidateName = scanner.nextLine();

                        System.out.print("Enter candidate's party: ");
                        String party = scanner.nextLine();

                        System.out.print("Enter candidate's platform: ");
                        String platform = scanner.nextLine();

                        Candidate candidate = new Candidate(candidateName, party, platform);

                        String insertCandidateQuery = "INSERT INTO candidates (name, party, platform) VALUES (?, ?, ?)";

                        PreparedStatement candidateStatement = connection.prepareStatement(insertCandidateQuery);

                        candidateStatement.setString(1, candidateName);
                        candidateStatement.setString(2, party);
                        candidateStatement.setString(3, platform);

                        int candidateRowsAffected = candidateStatement.executeUpdate();

                        if (candidateRowsAffected > 0) {
                            System.out.println("Candidate data inserted successfully.");
                        } else {
                            System.out.println("Candidate data insertion failed.");
                        }
                        Logger.log("Registered candidate: " + candidate.getName());
                        break;

                    case 3:
                        // Create a Poll
                        System.out.print("Enter poll question: ");
                        String question = scanner.nextLine();

                        System.out.print("Enter option 1: ");
                        String option1 = scanner.nextLine();

                        System.out.print("Enter option 2: ");
                        String option2 = scanner.nextLine();

                        Poll poll = new Poll(question, option1, option2);

                        String insertPollQuery = "INSERT INTO polls (question, option1, option2) VALUES (?, ?, ?)";

                        PreparedStatement pollStatement = connection.prepareStatement(insertPollQuery);

                        pollStatement.setString(1, question);
                        pollStatement.setString(2, option1);
                        pollStatement.setString(3, option2);

                        int pollRowsAffected = pollStatement.executeUpdate();

                        if (pollRowsAffected > 0) {
                            System.out.println("Poll data inserted successfully.");
                        } else {
                            System.out.println("Poll data insertion failed.");
                        }
                        Logger.log("Created a poll: " + poll.getQuestion());
                        break;

                    case 4:
                        // Record Election Results
                        System.out.print("Enter election name: ");
                        String electionName = scanner.nextLine();

                        System.out.print("Enter the name of the winning candidate: ");
                        String winnerName = scanner.nextLine();

                        System.out.print("Enter the number of votes for the winner: ");
                        int votesWinner = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        Result result = new Result(electionName, winnerName, votesWinner);

                        String insertResultQuery = "INSERT INTO results (election_name, winner_name, votes_winner) VALUES (?, ?, ?)";

                        PreparedStatement resultStatement = connection.prepareStatement(insertResultQuery);

                        resultStatement.setString(1, electionName);
                        resultStatement.setString(2, winnerName);
                        resultStatement.setInt(3, votesWinner);

                        int resultRowsAffected = resultStatement.executeUpdate();

                        if (resultRowsAffected > 0) {
                            System.out.println("Election results recorded successfully.");
                        } else {
                            System.out.println("Election results recording failed.");
                        }
                        Logger.log("Recorded election results for: " + result.getElectionName());
                        break;

                    case 5:
                        // Exit the application
                        Logger.log("Online Voting System Application Closed");
                        return;

                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
