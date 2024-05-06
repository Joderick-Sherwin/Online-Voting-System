import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementHelper {
    public static boolean executeInsertQuery(Connection connection, String query, Object... values) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // You can add more methods for other database operations with prepared statements
}
