package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        List.of(new User("Максим", "Николаев", (byte) 24),
                        new User("Маргарита", "Лебедева", (byte) 21),
                        new User("Олег", "Коровин", (byte) 35),
                        new User("Матвей", "Морозов", (byte) 27))
                .forEach(userService::saveUser);
        System.out.println(userService.getAllUsers());

        userService.cleanUsersTable();
        userService.dropUsersTable();
        try {
            Util.closeConnection(Util.connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
