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

public class VotingPortal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create an instance of VotingPortalApp
            VotingPortalApp app = new VotingPortalApp();
            app.showLogin();
        });
    }
}

