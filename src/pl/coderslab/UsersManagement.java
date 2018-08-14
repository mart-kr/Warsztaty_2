package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UsersManagement {

    public static void main(String[] args) {

        try (DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"); Scanner userInput = new Scanner(System.in)) {
            boolean keepWorking = true;

            while (keepWorking) {
                ArrayList<User> allUsers = User.loadAllUsers(connection.getConnection());
                for (User user : allUsers) {
                    System.out.println(user);
                }
                System.out.println();
                String userAction = getUserAction(userInput);

                if (userAction.equals("add")) {
                    User user = getNewUserData(connection.getConnection(), userInput);
                    user.saveToDB(connection.getConnection());
                } else if (userAction.equals("edit")) {
                    User user = getUserDataToEdit(connection.getConnection(), userInput);
                    if (user != null) {
                        user.saveToDB(connection.getConnection());
                    } else {
                        System.out.println("Użytkownik o podanym id nie istnieje.");
                    }
                } else if (userAction.equals("delete")) {
                    User user = getUserDataToDelete(connection.getConnection(), userInput);
                    if (user != null) {
                        user.delete(connection.getConnection());
                    } else {
                        System.out.println("Użytkownik o podanym id nie istnieje.");
                    }
                } else {
                    keepWorking = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getUserAction(Scanner userInput) {
        String userAction = "";

        while (!userAction.equals("add") && !userAction.equals("edit") && !userAction.equals("delete") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie użytkownika,\nedit – edycja użytkownika,\ndelete – usunięcie użytkownika,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    private static User getNewUserData(Connection connection, Scanner userInput) throws SQLException{
        User user = new User();
        System.out.println("Podaj email:");
        user.setEmail(userInput.nextLine());
        System.out.println("Podaj imię:");
        user.setUserName(userInput.nextLine());
        System.out.println("Podaj hasło:");
        user.setPassword(userInput.nextLine());
        verifyGroupId(connection, userInput, user);
        return user;
    }

    private static User getUserDataToEdit(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        User user = User.loadUserById(connection, userInput.nextInt());

        if (user != null) {
            userInput.nextLine();
            System.out.println("Podaj email:");
            user.setEmail(userInput.nextLine());
            System.out.println("Podaj imię:");
            user.setUserName(userInput.nextLine());
            System.out.println("Podaj hasło:");
            user.setPassword(userInput.nextLine());
            user.setGroupId(0);
            verifyGroupId(connection, userInput, user);
            return user;
        } else {
            return null;
        }

    }

    private static void verifyGroupId(Connection connection, Scanner userInput, User user) throws SQLException {
        while (user.getGroupId() == 0) {
            System.out.println("Podaj id grupy:");
            while (!userInput.hasNextInt()) {
                userInput.next();
                System.out.println("Podaj id!");
            }
            int groupId = userInput.nextInt();
            if (Group.loadGroupById(connection, groupId) != null) {
                userInput.nextLine();
                user.setGroupId(groupId);
            } else {
                System.out.println("Grupa o podanym id nie istnieje.");
            }
        }
    }

    private static User getUserDataToDelete(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        int id = userInput.nextInt();
        userInput.nextLine();
        return User.loadUserById(connection, id);
    }

}
