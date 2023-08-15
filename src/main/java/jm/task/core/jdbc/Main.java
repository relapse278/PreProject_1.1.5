package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

// 20230815 Arbeit

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь

        UserService userService = new UserServiceImpl();

        // Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует.
        userService.createUsersTable();

        // Добавление 4 User(ов) в таблицу с данными на свой выбор.
        userService.saveUser("Bob", "Evers", (byte)12);
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
    }
}