package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import jm.task.core.jdbc.util.Util;

public class UserDaoJDBCImpl implements UserDao {
    private static final String SQL_SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_CLEAN_USERS_TABLE = "DELETE FROM users";
    private static final String SQL_DROP_USERS_TABLE = "DROP TABLE IF EXISTS users";
    private static final String SQL_REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String SQL_INSERT_USER = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
    private static final String SQL_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users (name varchar(64) NOT NULL, id BIGINT AUTO_INCREMENT, age SMALLINT NOT NULL, lastName varchar(64) NOT NULL, CONSTRAINT primary_key primary key (id))";

    public UserDaoJDBCImpl() { }

    public void createUsersTable() {
        try (Connection connection = Util.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException eSQL) {
            System.err.format("SQL State: %s\n%s", eSQL.getSQLState(), eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DROP_USERS_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException eSQL) {
            System.err.format("SQL State: %s\n%s", eSQL.getSQLState(), eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            int numberOfUsersSaved = preparedStatement.executeUpdate();

            System.out.println(numberOfUsersSaved + " user(s) saved.");

        } catch (SQLException eSQL) {
            System.err.format("SQL State: %s\n%s", eSQL.getSQLState(), eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_USER_BY_ID)) {

            preparedStatement.setLong(1, id);
            int numberOfUsersRemoved = preparedStatement.executeUpdate();

            System.out.println(numberOfUsersRemoved + " user(s) removed.");
        } catch (SQLException eSQL) {
            System.err.format("SQL State: %s\n%s", eSQL.getSQLState(), eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {

        List<User> listOfAllUsers = new ArrayList<>();

        try (Connection connection = Util.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

//            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));

                listOfAllUsers.add(user);
            }
        } catch (SQLException eSQL) {
            System.err.format("SQL State: %s\n%s", eSQL.getSQLState(), eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOfAllUsers;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CLEAN_USERS_TABLE)) {

            int numberOfRowsDeleted = preparedStatement.executeUpdate();

            System.out.println(numberOfRowsDeleted + " row(s) deleted.");
        } catch (SQLException eSQL) {
            System.err.format("SQL State: %s\n%s", eSQL.getSQLState(), eSQL.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}