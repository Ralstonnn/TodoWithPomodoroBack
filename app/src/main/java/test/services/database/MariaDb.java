package test.services.database;

import org.mariadb.jdbc.MariaDbPoolDataSource;
import test.services.Env;

import java.sql.*;


public class MariaDb {
    private static MariaDbPoolDataSource DATA_SOURCE;
    private static final String CONNECTION_STRING = "jdbc:mariadb://" + Env.get("DB_URL") + "/" + Env.get("DB_NAME");
    private static final String USER = Env.get("DB_USER");
    private static final String PASSWORD = Env.get("DB_PASSWORD");

    public static void init() {
        try {
            DATA_SOURCE = new MariaDbPoolDataSource(CONNECTION_STRING + "?user=" + USER + "&password=" + PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static ResultSet query(String query) throws SQLException {
        try (Connection con = getConnection(); PreparedStatement statement = con.prepareStatement(query)) {
            return statement.executeQuery();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
