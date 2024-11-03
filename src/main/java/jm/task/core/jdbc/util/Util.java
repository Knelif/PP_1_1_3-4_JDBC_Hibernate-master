package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String propsDir = "src/main/resources/database.properties";
    private static String dbUrl = "Default";
    private static String userName = "Default";
    private static String password = "Default";

    static {
        try (InputStream in = Files.newInputStream(Paths.get(propsDir))) {
            Properties props = new Properties();
            props.load(in);
            dbUrl = props.getProperty("url");
            userName = props.getProperty("username");
            password = props.getProperty("password");
        } catch (IOException e) {
            System.out.printf("Error reading properties file in directory: %s. Properties values set as default ", propsDir);
        }
    }

    private static  Connection connection;

    private Util() {

    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        return getConnection(dbUrl, userName, password);
    }

    public static Connection getConnection(String dbUrl, String userName, String password) throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) return connection;
        else {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, userName, password);
            return connection;
        }
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
