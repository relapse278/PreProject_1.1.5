package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Bob", "Evans", (byte)12);
        userService.saveUser("Rob", "Mertens", (byte) 25);
        userService.saveUser("Ann", "Meyer", (byte) 36);
        userService.saveUser("Britney", "Spears", (byte) 47);

        List<User> listOfAllUser = userService.getAllUsers();
        listOfAllUser.forEach(System.out::println);

        userService.removeUserById(2);

        List<User> listOfAllUser2 = userService.getAllUsers();
        listOfAllUser2.forEach(System.out::println);

        userService.cleanUsersTable();

        List<User> listOfAllUser3 = userService.getAllUsers();
        listOfAllUser3.forEach(System.out::println);

        userService.dropUsersTable();

        userService.closeDb();
    }
}