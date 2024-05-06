import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdvancedMasterApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Master App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(960, 1080);
            frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            // Welcome address
            JTextArea welcomeArea = new JTextArea("Welcome to the Online Voting System!\n\n" +
                    "We are delighted to have you here as we embark on a journey of civic engagement and democratic participation. " +
                    "This platform is designed to make the voting process seamless and accessible to all. Whether you are an " +
                    "administrator overseeing elections, a voter ready to cast your ballot, or an enthusiast checking out the " +
                    "election results, our system is here to empower you. Thank you for being a part of this digital democracy, " +
                    "and we hope you find your experience both convenient and meaningful.");
            welcomeArea.setEditable(false);
            welcomeArea.setWrapStyleWord(true);
            welcomeArea.setLineWrap(true);
            welcomeArea.setPreferredSize(new Dimension(800, 400));
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
