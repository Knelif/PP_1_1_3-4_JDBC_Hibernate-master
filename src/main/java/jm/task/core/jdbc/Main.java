package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        User user1 = new User("Максим", "Николаев", (byte) 24);
        User user2 = new User("Маргарита", "Лебедева", (byte) 21);
        User user3 = new User("Олег", "Коровин", (byte) 35);
        User user4 = new User("Матвей", "Морозов", (byte) 27);
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
        userService.saveUser(user4);
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
