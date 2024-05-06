import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.onlinevotingsystem.db.*;

public class VoterLoginApp {

    public static void main(String[] args) {
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
                    String voterId = voterIdField.getText();
                    String dob = dobField.getText();

                    // Authenticate voter against the database
                    if (authenticateVoter(voterId, dob)) {
                        // If authentication is successful, open the second page
                        frame.dispose(); // Close the first page
                        showSecurityQuestionPage(voterId); // Open the second page
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

    private static void showSecurityQuestionPage(String voterId) {
        JFrame frame = new JFrame("Security Question");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel securityQuestionLabel = new JLabel("Security Question:");
        JLabel securityQuestionDisplay = new JLabel(fetchSecurityQuestion(voterId));

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
                    // TODO: Add logic to proceed to the voting page
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid Security Answer. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private static boolean authenticateVoter(String voterId, String dob) {
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

    private static String fetchSecurityQuestion(String voterId) {
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

    private static boolean verifySecurityAnswer(String voterId, String securityAnswer) {
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
}
