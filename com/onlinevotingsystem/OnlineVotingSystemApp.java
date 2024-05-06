import com.onlinevotingsystem.candidate.Candidate;
import com.onlinevotingsystem.poll.Poll;
import com.onlinevotingsystem.registration.Voter;
import com.onlinevotingsystem.result.Result;
import com.onlinevotingsystem.db.DBConnection;
import com.onlinevotingsystem.db.DBInitializer;
import com.onlinevotingsystem.util.InputValidator;
import com.onlinevotingsystem.util.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class OnlineVotingSystemApp {
    public static void main(String[] args) {
        // Initialize the database (create tables)
        Connection connection = null; // Declare the connection variable outside the try block
        try {
            // Initialize the database (create tables)
            DBInitializer.initializeDatabase();

            connection = DBConnection.getConnection();
            // Your application logic starts here
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

                        // Additional input validation can be added here
                        if (InputValidator.isEmailValid(email)) {
                            Voter voter = new Voter(name, email, voterID, address, dateOfBirth);
                            // Call the method to register the voter in the database
                            // VoterRegistrationApp.registerVoter(connection, voter);
                            Logger.log("Registered voter: " + voter.getName());
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
                        // Call the method to register the candidate in the database
                        // CandidateRegistrationApp.registerCandidate(connection, candidate);
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
                        // Call the method to create a poll in the database
                        // PollingApp.createPoll(connection, poll);
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
                        // Call the method to record election results in the database
                        // ResultApp.recordElectionResult(connection, result);
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
}