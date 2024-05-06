import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MasterApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Master App");
			
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(960, 540);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
			frame.setLocationRelativeTo(null);

            // Welcome address
            JTextArea welcomeArea = new JTextArea("Welcome to the Online Voting System!\n\n" +
                    "We extend a warm welcome to you as you join us in utilizing the power of technology to shape the future of democracy. " +
                    "Our Online Voting System is not just a platform; it's a commitment to making the democratic process more inclusive and " +
                    "convenient for everyone.\n\n" +
                    "In this digital space, administrators can efficiently manage elections, voters can exercise their civic duty with " +
                    "ease, and enthusiasts can stay updated on the latest election results. Whether you are a first-time voter or a seasoned " +
                    "administrator, our goal is to provide you with a seamless and secure experience.\n\n" +
                    "As you explore the various features of our platform, feel free to engage with the democratic process and make your " +
                    "voice heard. Thank you for choosing the Online Voting System. Your participation is key to the success of this digital " +
                    "democracy, and we hope you have a fulfilling and empowering experience.");

            welcomeArea.setEditable(false);
            welcomeArea.setWrapStyleWord(true);
            welcomeArea.setLineWrap(true);
            welcomeArea.setPreferredSize(new Dimension(800, 400));
            welcomeArea.setFont(new Font("Arial", Font.PLAIN, 16));

            // Center align the title
            welcomeArea.setAlignmentX(Component.CENTER_ALIGNMENT);
            frame.add(welcomeArea);

            JButton adminButton = new JButton("Admin App");
            JButton voterButton = new JButton("Voter Login App");
            JButton votingButton = new JButton("Voting Portal");
            JButton resultsButton = new JButton("Election Results App");

            // Set preferred size for one button
            Dimension buttonSize = new Dimension(150, 40);
            adminButton.setPreferredSize(buttonSize);

            // Apply preferred size to other buttons
            voterButton.setPreferredSize(buttonSize);
            votingButton.setPreferredSize(buttonSize);
            resultsButton.setPreferredSize(buttonSize);

            frame.add(adminButton);
            frame.add(voterButton);
            frame.add(votingButton);
            frame.add(resultsButton);

            adminButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    launchAdminApp();
                }
            });

            voterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    launchVoterLoginApp();
                }
            });

            votingButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    launchVotingPortal();
                }
            });

            resultsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    launchElectionResultsApp();
                }
            });

            frame.setVisible(true);
        });
    }

    private static void launchAdminApp() {
        SwingUtilities.invokeLater(() -> {
            AdminApp adminApp = new AdminApp();
            adminApp.main(new String[]{});
        });
    }

    private static void launchVoterLoginApp() {
        SwingUtilities.invokeLater(() -> {
            VoterLoginApp voterLoginApp = new VoterLoginApp();
            voterLoginApp.main(new String[]{});
        });
    }

    private static void launchVotingPortal() {
        SwingUtilities.invokeLater(() -> {
            VotingPortal votingPortal = new VotingPortal();
            votingPortal.main(new String[]{});
        });
    }

    private static void launchElectionResultsApp() {
        SwingUtilities.invokeLater(() -> {
            ElectionResultsApp electionResultsApp = new ElectionResultsApp();
            electionResultsApp.main(new String[]{});
        });
    }
}
