package jm.task.core.jdbc.service;

import jm.task.core.jdbc.model.User;

import java.util.List;

public interface UserService {
    void createUsersTable();

    void dropUsersTable();

    void saveUser(String name, String lastName, byte age);

    default void saveUser(User user) {
        saveUser(user.getName(), user.getLastName(), user.getAge());
    }

    void removeUserById(long id);

    List<User> getAllUsers();

    void cleanUsersTable();
}
