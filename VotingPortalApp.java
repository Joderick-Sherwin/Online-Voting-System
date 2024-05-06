import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.onlinevotingsystem.db.*;

public class VotingPortalApp {

    private boolean loginSuccessful = false;
    private String voterId; // Store the voter ID after successful login
    private String securityQuestion; // Store the security question for verification

    public void showLogin() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Voter Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200); // Adjusted size
            frame.setLayout(new BorderLayout());
			frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10)); // Adjusted grid spacing
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adjusted border

            JLabel voterIdLabel = new JLabel("Voter ID:");
            JTextField voterIdField = new JTextField();

            JLabel dobLabel = new JLabel("Date of Birth:");
            JTextField dobField = new JTextField();

            JButton loginButton = new JButton("Login");

            panel.add(voterIdLabel);
            panel.add(voterIdField);
            panel.add(dobLabel);
            panel.add(dobField);
            panel.add(new JLabel()); // Empty label for spacing
            panel.add(loginButton);

            frame.add(panel, BorderLayout.CENTER);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String enteredVoterId = voterIdField.getText();
                    String dob = dobField.getText();

                    // Fetch security question for the entered voter ID
                    securityQuestion = fetchSecurityQuestion(enteredVoterId);

                    // Authenticate voter against the database
                    if (authenticateVoter(enteredVoterId, dob)) {
                        // If authentication is successful, open the second page
                        frame.dispose(); // Close the first page
                        showSecurityQuestionPage(enteredVoterId); // Open the second page
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Invalid Voter ID or Date of Birth",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            frame.setVisible(true);
        });
    }

    private void showSecurityQuestionPage(String voterId) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Security Question");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel securityQuestionLabel = new JLabel("Security Question:");
            JLabel securityQuestionDisplay = new JLabel(securityQuestion);

            JLabel securityAnswerLabel = new JLabel("Security Answer:");
            JTextField securityAnswerField = new JTextField();

            JButton verifyButton = new JButton("Verify");

            panel.add(securityQuestionLabel);
            panel.add(securityQuestionDisplay);
            panel.add(securityAnswerLabel);
            panel.add(securityAnswerField);
            panel.add(new JLabel()); // Empty label for spacing
            panel.add(verifyButton);

            frame.add(panel, BorderLayout.CENTER);

            verifyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String securityAnswer = securityAnswerField.getText();
                    if (verifySecurityAnswer(voterId, securityAnswer)) {
                        JOptionPane.showMessageDialog(frame,
                                "Security Answer Verified. You can proceed to vote.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose(); // Close the second page
                        showVotingPortal(voterId); // Proceed to the voting portal
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Invalid Security Answer. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            frame.setVisible(true);
        });
    }

    private void showVotingPortal(String voterId) {
        SwingUtilities.invokeLater(() -> {
            JFrame votingFrame = new JFrame("Voting Portal");
            votingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            votingFrame.setSize(600, 400);
            votingFrame.setLayout(new BorderLayout());

            JPanel candidatePanel = new JPanel(new GridLayout(0, 1, 10, 10));
            candidatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            List<String> candidateNames = getCandidateNames();

            // Create radio buttons for each candidate
            ButtonGroup candidateGroup = new ButtonGroup();
            for (String candidateName : candidateNames) {
                JRadioButton candidateButton = new JRadioButton(candidateName);
                candidateGroup.add(candidateButton);
                candidatePanel.add(candidateButton);
            }

            // Add "Nota" option
            JRadioButton notaButton = new JRadioButton("Nota");
            candidateGroup.add(notaButton);
            candidatePanel.add(notaButton);

            JButton voteButton = new JButton("Vote");

            votingFrame.add(candidatePanel, BorderLayout.CENTER);
            votingFrame.add(voteButton, BorderLayout.SOUTH);

            voteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the selected candidate or "Nota"
                    String selectedCandidate = getSelectedCandidate(candidateGroup);

                    if (selectedCandidate != null) {
                        // Check if the voter has already voted
                        if (!hasVoterVoted(voterId)) {
                            recordVote(voterId, selectedCandidate);
                            JOptionPane.showMessageDialog(votingFrame,
                                    "Vote cast for: " + selectedCandidate,
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            // You can add further logic here if needed
                            // Force logout after vote is cast
                            loginSuccessful = false;
                            votingFrame.dispose();
                            // Show login screen again
                            showLogin();
                        } else {
                            JOptionPane.showMessageDialog(votingFrame,
                                    "You have already voted. Multiple votes are not allowed.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            // Force logout
                            loginSuccessful = false;
                            votingFrame.dispose();
                            // Show login screen again
                            showLogin();
                        }
                    } else {
                        JOptionPane.showMessageDialog(votingFrame,
                                "Please select a candidate or 'Nota' to cast your vote",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            votingFrame.setVisible(true);
        });
    }

    private boolean authenticateVoter(String voterId, String dob) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM voters WHERE voter_id = ? AND date_of_birth = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, voterId);
                preparedStatement.setString(2, dob);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // Returns true if voter is authenticated
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String fetchSecurityQuestion(String voterId) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT security_question FROM voters WHERE voter_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, voterId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("security_question");
                    }
                }
            }
            // Add a default return statement in case no question is found
            return "Security question not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching security question"; // Return a default message in case of an error
        }
    }

    private boolean verifySecurityAnswer(String voterId, String securityAnswer) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM voters WHERE voter_id = ? AND security_answer = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, voterId);
                preparedStatement.setString(2, securityAnswer);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // Returns true if security answer is valid
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<String> getCandidateNames() {
        List<String> candidateNames = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT name FROM candidates";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        candidateNames.add(resultSet.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidateNames;
    }

    private String getSelectedCandidate(ButtonGroup candidateGroup) {
        for (Enumeration<AbstractButton> buttons = candidateGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }

    private boolean hasVoterVoted(String voterId) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM appvotes WHERE voter_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, voterId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // Returns true if the voter has voted
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void recordVote(String voterId, String selectedCandidate) {
        try (Connection connection = DBConnection.getConnection()) {
            // Get the candidate ID for the selected candidate
            String candidateIdQuery = "SELECT id FROM candidates WHERE name = ?";
            int candidateId = -1; // Default value for error handling
            try (PreparedStatement candidateIdStatement = connection.prepareStatement(candidateIdQuery)) {
                candidateIdStatement.setString(1, selectedCandidate);
                try (ResultSet candidateIdResult = candidateIdStatement.executeQuery()) {
                    if (candidateIdResult.next()) {
                        candidateId = candidateIdResult.getInt("id");
                    }
                }
            }

            if (candidateId != -1) {
                // Record the vote in the votes table
                String recordVoteQuery = "INSERT INTO appvotes (voter_id, candidate_id) VALUES (?, ?)";
                try (PreparedStatement recordVoteStatement = connection.prepareStatement(recordVoteQuery)) {
                    recordVoteStatement.setString(1, voterId);
                    recordVoteStatement.setInt(2, candidateId);
                    recordVoteStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new VotingPortalApp().showLogin();
    }
}
