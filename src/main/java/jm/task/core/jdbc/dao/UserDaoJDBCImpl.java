package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class UserDaoJDBCImpl implements UserDao {
    private static final String _createUserTableSQL = """
            CREATE TABLE IF NOT EXISTS user (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name CHAR(20),
                lastname CHAR(20),
                age TINYINT
                )""";
    private static final String _dropUserTableSQL = "DROP TABLE IF EXISTS user";
    private static final String _truncateUserTableSQL = "TRUNCATE TABLE user";
    private static final String _selectAllUsersSQL = "SELECT * FROM user";
    private static final String _insertUserSQL = "INSERT INTO user (name, lastname, age) VALUES (?,?,?)";
    private static final String _deleteUserByIdSQL = "DELETE FROM user WHERE id = ?";

    private final Connection connection;


    public UserDaoJDBCImpl() {
        try {
            connection = Util.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            statement.executeUpdate(_createUserTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            statement.executeUpdate(_dropUserTableSQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            Savepoint savepoint = connection.setSavepoint();
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(_insertUserSQL)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                messegeQuerry(String.format("User \"%s %s\" age - %d successfully added", name, lastName, age), _insertUserSQL, preparedStatement.executeUpdate());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savepoint);
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            Savepoint savepoint = connection.setSavepoint();
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(_deleteUserByIdSQL)) {
                preparedStatement.setLong(1, id);
                messegeQuerry("User was deleted by id " + id, _deleteUserByIdSQL, preparedStatement.executeUpdate());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savepoint);
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(true);
            ResultSet resultSet = statement.executeQuery(_selectAllUsersSQL);
            List<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
            return userList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(_truncateUserTableSQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void messegeQuerry(String messege, String querry, int rows) {
        System.out.printf("#DB_INF: %s. Querry: \"%s\". Rows: %d\n", messege, querry, rows);
    }

    private void messegeQuerry(String messege, String querry) {
        System.out.printf("#DB_INF: %s. Querry: \"%s\"\n", messege, querry);
    }
}
