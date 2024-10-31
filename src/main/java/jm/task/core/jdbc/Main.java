package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        List<User> userList = new LinkedList<>();
        userList.add(new User("Максим", "Николаев", (byte) 24));
        userList.add( new User("Маргарита", "Лебедева", (byte) 21));
        userList.add( new User("Олег", "Коровин", (byte) 35));
        userList.add(new User("Матвей", "Морозов", (byte) 27));

        userList.forEach(userService::saveUser);

        System.out.println(userService.getAllUsers());

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
