import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Timer;
import com.onlinevotingsystem.candidate.Candidate;
import com.onlinevotingsystem.db.DBConnection;

public class AdminApp {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static boolean electionsEnded = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Admin Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(300, 150);
            loginFrame.setLayout(new BorderLayout());
			loginFrame.setLocationRelativeTo(null);

            JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            loginPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel usernameLabel = new JLabel("Username:");
            JTextField usernameField = new JTextField();

            JLabel passwordLabel = new JLabel("Password:");
            JPasswordField passwordField = new JPasswordField();

            JButton loginButton = new JButton("Login");

            loginPanel.add(usernameLabel);
            loginPanel.add(usernameField);
            loginPanel.add(passwordLabel);
            loginPanel.add(passwordField);
            loginPanel.add(new JLabel()); // Empty label for spacing
            loginPanel.add(loginButton);

            loginFrame.add(loginPanel, BorderLayout.CENTER);

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    String password = String.valueOf(passwordField.getPassword());

                    if (isAdminAuthenticated(username, password)) {
                        loginFrame.dispose();
                        showAdminInterface();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame,
                                "Invalid username or password",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            loginFrame.setVisible(true);
        });
    }

    private static boolean isAdminAuthenticated(String username, String password) {
        return username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD);
    }

    private static void showAdminInterface() {
        JFrame frame = new JFrame("Admin Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        JButton addCandidateButton = new JButton("Add Candidate");
        JButton removeCandidateButton = new JButton("Remove Candidate");
        JButton updateCandidateButton = new JButton("Update Candidate");
        JButton viewVotesButton = new JButton("View Votes");
        JButton endElectionsButton = new JButton("End Elections");
        JButton publishResultButton = new JButton("Publish Result");
        JButton newElectionsButton = new JButton("New Elections");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addCandidateButton);
        buttonPanel.add(removeCandidateButton);
        buttonPanel.add(updateCandidateButton);
        buttonPanel.add(viewVotesButton);
        buttonPanel.add(endElectionsButton);
        buttonPanel.add(publishResultButton);
        buttonPanel.add(newElectionsButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Display existing candidates
        displayCandidates(logArea);

        // Display votes received by candidates
        displayVotes(logArea);

        // Set up a timer to refresh the data every 10 seconds
        Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh the displayed candidates and votes
                displayCandidates(logArea);
                displayVotes(logArea);
            }
        });
        timer.setInitialDelay(0); // Start immediately
        timer.start();

        addCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("Add Candidate button clicked\n");
                addCandidate();
                // Refresh the candidate list after adding a new candidate
                displayCandidates(logArea);
            }
        });

        removeCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("Remove Candidate button clicked\n");
                String candidateIdString = JOptionPane.showInputDialog(frame, "Enter Candidate ID to Remove:");
                if (candidateIdString != null && !candidateIdString.isEmpty()) {
                    try {
                        int candidateId = Integer.parseInt(candidateIdString);
                        if (removeCandidate(candidateId)) {
                            logArea.append("Candidate removed successfully.\n");
                            // Refresh the candidate list after removing a candidate
                            displayCandidates(logArea);
                        } else {
                            logArea.append("Failed to remove candidate.\n");
                        }
                    } catch (NumberFormatException ex) {
                        logArea.append("Invalid Candidate ID format.\n");
                    }
                } else {
                    logArea.append("Candidate ID is required.\n");
                }
            }
        });

        updateCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("Update Candidate button clicked\n");
                String candidateIdString = JOptionPane.showInputDialog(frame, "Enter Candidate ID to Update:");
                if (candidateIdString != null && !candidateIdString.isEmpty()) {
                    try {
                        int candidateId = Integer.parseInt(candidateIdString);
                        updateCandidate(candidateId);
                        // Refresh the candidate list after updating a candidate
                        displayCandidates(logArea);
                    } catch (NumberFormatException ex) {
                        logArea.append("Invalid Candidate ID format.\n");
                    }
                } else {
                    logArea.append("Candidate ID is required.\n");
                }
            }
        });

        viewVotesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("View Votes button clicked\n");
                // Display votes received by candidates
                displayVotes(logArea);
            }
        });

        endElectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("End Elections button clicked\n");
                endElections();
                electionsEnded = true;
            }
        });

        publishResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("Publish Result button clicked\n");
                publishResult();
            }
        });

        newElectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logArea.append("New Elections button clicked\n");
                newElections();
                electionsEnded = false;
            }
        });

        frame.setVisible(true);
    }

    private static boolean addCandidate() {
        JFrame addFrame = new JFrame("Add Candidate");
        addFrame.setSize(300, 150);
        addFrame.setLayout(new BorderLayout());
		addFrame.setLocationRelativeTo(null);

        JPanel addPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel partyLabel = new JLabel("Party:");
        JTextField partyField = new JTextField();

        JLabel platformLabel = new JLabel("Platform:");
        JTextField platformField = new JTextField();

        JButton addButton = new JButton("Add");

        addPanel.add(nameLabel);
        addPanel.add(nameField);
        addPanel.add(partyLabel);
        addPanel.add(partyField);
        addPanel.add(platformLabel);
        addPanel.add(platformField);
        addPanel.add(new JLabel()); // Empty label for spacing
        addPanel.add(addButton);

        addFrame.add(addPanel, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String party = partyField.getText();
                String platform = platformField.getText();

                // Create Candidate object
                Candidate candidate = new Candidate(name, party, platform);

                // Add candidate to the database
                if (addCandidateToDatabase(candidate)) {
                    JOptionPane.showMessageDialog(addFrame,
                            "Candidate added successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    addFrame.dispose(); // Close add window after successful addition
                } else {
                    JOptionPane.showMessageDialog(addFrame,
                            "Failed to add candidate",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addFrame.setVisible(true);
        return false;
    }

    private static boolean addCandidateToDatabase(Candidate candidate) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "INSERT INTO candidates (name, party, platform) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, candidate.getName());
                preparedStatement.setString(2, candidate.getParty());
                preparedStatement.setString(3, candidate.getPlatform());
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean removeCandidate(int candidateId) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "DELETE FROM candidates WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, candidateId);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void updateCandidate(int candidateId) {
        JFrame updateFrame = new JFrame("Update Candidate");
        updateFrame.setSize(300, 150);
        updateFrame.setLayout(new BorderLayout());
		updateFrame.setLocationRelativeTo(null);

        JPanel updatePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        updatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel partyLabel = new JLabel("Party:");
        JTextField partyField = new JTextField();

        JLabel platformLabel = new JLabel("Platform:");
        JTextField platformField = new JTextField();

        JButton updateButton = new JButton("Update");

        updatePanel.add(nameLabel);
        updatePanel.add(nameField);
        updatePanel.add(partyLabel);
        updatePanel.add(partyField);
        updatePanel.add(platformLabel);
        updatePanel.add(platformField);
        updatePanel.add(new JLabel()); // Empty label for spacing
        updatePanel.add(updateButton);

        updateFrame.add(updatePanel, BorderLayout.CENTER);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String party = partyField.getText();
                String platform = platformField.getText();

                // Update candidate details in the database
                if (updateCandidateDetails(candidateId, name, party, platform)) {
                    JOptionPane.showMessageDialog(updateFrame,
                            "Candidate details updated successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateFrame.dispose(); // Close update window after successful update
                } else {
                    JOptionPane.showMessageDialog(updateFrame,
                            "Failed to update candidate details",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateFrame.setVisible(true);
    }

    private static boolean updateCandidateDetails(int candidateId, String name, String party, String platform) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "UPDATE candidates SET name = ?, party = ?, platform = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, party);
                preparedStatement.setString(3, platform);
                preparedStatement.setInt(4, candidateId);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static void displayCandidates(JTextArea logArea) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM candidates";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    logArea.setText(""); // Clear the existing content
                    logArea.append("Existing Candidates:\n");
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String party = resultSet.getString("party");
                        String platform = resultSet.getString("platform");
                        logArea.append("ID: " + id + ", Name: " + name + ", Party: " + party + ", Platform: " + platform + "\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void displayVotes(JTextArea logArea) {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT c.name, COUNT(v.candidate_id) as vote_count " +
                           "FROM candidates c " +
                           "LEFT JOIN appvotes v ON c.id = v.candidate_id " +
                           "GROUP BY c.id, c.name";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    logArea.append("\nVotes Received:\n");
                    while (resultSet.next()) {
                        String candidateName = resultSet.getString("name");
                        int voteCount = resultSet.getInt("vote_count");
                        logArea.append("Candidate: " + candidateName + ", Votes: " + voteCount + "\n");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

private static void endElections() {
    try (Connection connection = DBConnection.getConnection()) {
        // Delete votes
        String deleteVotesQuery = "DELETE FROM appvotes";
        try (PreparedStatement deleteVotesStatement = connection.prepareStatement(deleteVotesQuery)) {
            deleteVotesStatement.executeUpdate();
        }

		// Delete candidates
        String deletecmdVotesQuery = "DELETE FROM votes";
        try (PreparedStatement deletecmdVotesStatement = connection.prepareStatement(deletecmdVotesQuery)) {
            deletecmdVotesStatement.executeUpdate();
        }

        // Delete candidates
        String deleteCandidatesQuery = "DELETE FROM candidates";
        try (PreparedStatement deleteCandidatesStatement = connection.prepareStatement(deleteCandidatesQuery)) {
            deleteCandidatesStatement.executeUpdate();
        }

        JOptionPane.showMessageDialog(null, "Elections Ended", "Info", JOptionPane.INFORMATION_MESSAGE);
        electionsEnded = true;
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle database exceptions
        JOptionPane.showMessageDialog(null, "Error ending elections", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    private static void publishResult() {
        // Store election results with date and name
        String resultSummary = JOptionPane.showInputDialog("Enter Result Summary");;
        String electionName = JOptionPane.showInputDialog("Enter Election Name:");
        if (electionName != null && !electionName.isEmpty()) {
            storeResult(electionName, resultSummary);
        } else {
            JOptionPane.showMessageDialog(null, "Election Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void newElections() {
        // Clear votes for new elections
        clearVotes();
        // Perform actions to prepare for new elections
    }

    private static void clearVotes() {
        try (Connection connection = DBConnection.getConnection()) {
            // Clear the appvotes table
            String clearVotesQuery = "DELETE FROM appvotes";
            try (PreparedStatement clearVotesStatement = connection.prepareStatement(clearVotesQuery)) {
                clearVotesStatement.executeUpdate();
            }

            // Unfreeze the voting process
            electionsEnded = false;

            // Display message
            JOptionPane.showMessageDialog(null, "Votes cleared for new elections", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to clear votes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



	private static void storeResult(String electionName, String resultSummary) {
		try (Connection connection = DBConnection.getConnection()) {
			String query = "INSERT INTO election_results (election_name, result_summary) VALUES (?, ?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, electionName);
				preparedStatement.setString(2, resultSummary);
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(null, "Result stored for election: " + electionName, "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Failed to store result for election: " + electionName, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error storing result for election: " + electionName, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}



    private static String getResultSummary() {
        // ... (existing code)
        return "";
    }

}