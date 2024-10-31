package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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


    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(_createUserTableSQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(_dropUserTableSQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(_insertUserSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(_deleteUserByIdSQL);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
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

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(_truncateUserTableSQL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
