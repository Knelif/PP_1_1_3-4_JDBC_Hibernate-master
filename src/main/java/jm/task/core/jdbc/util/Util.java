package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
    private static final Properties dataBaseProperties = new Properties();
    private static final Configuration hiberConfiguration = new Configuration();

    static {
        try (InputStream in = Files.newInputStream(Paths.get(propsDir))) {
            dataBaseProperties.load(in);
        } catch (IOException e) {
            System.out.printf("Error reading properties file in directory: %s. Properties values set as default ", propsDir);
        }

        hiberConfiguration.addAnnotatedClass(User.class);
    }

    private static Connection connection;
    private static SessionFactory sessionFactory;

    private Util() {

    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        return getConnection(dataBaseProperties.getProperty("url"),
                dataBaseProperties.getProperty("username"),
                dataBaseProperties.getProperty("password"));
    }

    public static Connection getConnection(String dbUrl, String userName, String password) throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) return connection;
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(dbUrl, userName, password);
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = hiberConfiguration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void closeSessionFactory(SessionFactory sFactory) {
        if (sessionFactory != null && !sFactory.isClosed()) sFactory.close();
    }


}
