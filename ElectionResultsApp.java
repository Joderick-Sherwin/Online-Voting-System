import com.onlinevotingsystem.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ElectionResultsApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Election Results");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());
			frame.setLocationRelativeTo(null);
            JTextArea resultArea = new JTextArea();
            resultArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(resultArea);

            frame.add(scrollPane, BorderLayout.CENTER);

            // Display election results
            displayElectionResults(resultArea);

            frame.setVisible(true);
        });
    }

    private static void displayElectionResults(JTextArea resultArea) {
        try (Connection connection = DBConnection.getConnection()) {
            // Find the highest and second-highest votes
            String votesQuery = "SELECT candidate_id, COUNT(*) as vote_count FROM appvotes GROUP BY candidate_id ORDER BY vote_count DESC LIMIT 2";
            try (PreparedStatement votesStatement = connection.prepareStatement(votesQuery)) {
                try (ResultSet votesResult = votesStatement.executeQuery()) {
                    int highestVotes = 0;
                    int secondHighestVotes = 0;
                    String winnercandidate_id = "";
                    String runnerUpcandidate_id = "";

                    while (votesResult.next()) {
                        int voteCount = votesResult.getInt("vote_count");
                        String candidate_id = votesResult.getString("candidate_id");

                        if (voteCount > highestVotes) {
                            secondHighestVotes = highestVotes;
                            highestVotes = voteCount;
                            runnerUpcandidate_id = winnercandidate_id;
                            winnercandidate_id = candidate_id;
                        } else if (voteCount > secondHighestVotes) {
                            secondHighestVotes = voteCount;
                            runnerUpcandidate_id = candidate_id;
                        }
                    }

                    // Fetch winner's name
                    String winnerName = getCandidateNameById(winnercandidate_id);

                    // Display election results
                    String electionQuery = "SELECT * FROM election_results";
                    try (PreparedStatement electionStatement = connection.prepareStatement(electionQuery)) {
                        try (ResultSet resultSet = electionStatement.executeQuery()) {
                            resultArea.setText(""); // Clear the existing content
                            resultArea.append("Election Results:\n");
                            while (resultSet.next()) {
                                String electionName = resultSet.getString("election_name");
                                String resultSummary = resultSet.getString("result_summary");
                                Timestamp endDateTime = resultSet.getTimestamp("created_at");

                                resultArea.append("Election Name: " + electionName + "\n");
                                resultArea.append("End Date and Time: " + endDateTime + "\n");
                                resultArea.append("Result Summary: " + resultSummary + "\n");
                                resultArea.append("Winner: " + winnerName + "\n");
                                resultArea.append("Vote Difference: " + (highestVotes - secondHighestVotes) + "\n\n");
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Helper method to get the name of the winning candidate
    private static String getCandidateNameById(String candidate_id) {
        try (Connection connection = DBConnection.getConnection()) {
            // Retrieve candidate_id and name from candidates table
            String query = "SELECT id, name FROM candidates WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, candidate_id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Match candidate_id from appvotes with id from candidates
                        int candidateIdFromCandidates = resultSet.getInt("id");
                        return getWinnerCandidateName(candidateIdFromCandidates);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Candidate" + candidate_id; // Return a default name if not found
    }

    // Helper method to get the name of the winning candidate
    private static String getWinnerCandidateName(int winnerCandidateId) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT name FROM candidates WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, winnerCandidateId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("name");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Candidate" + winnerCandidateId; // Return a default name if not found
    }
}
