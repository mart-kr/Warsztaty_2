package pl.coderslab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UsersManagement {

    public static void main(String[] args) {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");
        try {
            boolean keepWorking = true;

            while (keepWorking) {
                ArrayList<User> allUsers = User.loadAllUsers(connection.getConnection());
                for (User user : allUsers) {
                    System.out.println(user);
                }
                System.out.println();
                String userAction = getUserAction();

                if (userAction.equals("add")) {
                    User user = getNewUserData();
                    user.saveToDB(connection.getConnection());
                } else if (userAction.equals("edit")) {
                    User user = getUserDataToEdit();
                    user.saveToDB(connection.getConnection());
                } else if (userAction.equals("delete")) {
                    User user = getUserDataToDelete();
                    user.delete(connection.getConnection());
                } else {
                    keepWorking = false;
                }

            }
            connection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserAction() {
        Scanner userInput = new Scanner(System.in);
        String userAction = "";

        while (!userAction.equals("add") && !userAction.equals("edit") && !userAction.equals("delete") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie użytkownika,\nedit – edycja użytkownika,\ndelete – edycja użytkownika,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    public static User getNewUserData() {
        User user = new User();
        System.out.println("Podaj email:");
        Scanner userInput = new Scanner(System.in);
        user.setEmail(userInput.nextLine());
        System.out.println("Podaj imię:");
        user.setUserName(userInput.nextLine());
        System.out.println("Podaj hasło:");
        user.setPassword(userInput.nextLine());
        System.out.println("Podaj id grupy:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        user.setGroupId(userInput.nextInt());
        return user;
    }

    public static User getUserDataToEdit() throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");

        System.out.println("Podaj id:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        User user = User.loadUserById(connection.getConnection(), userInput.nextInt());
        userInput.nextLine();
        System.out.println("Podaj email:");
        user.setEmail(userInput.nextLine());
        System.out.println("Podaj imię:");
        user.setUserName(userInput.nextLine());
        System.out.println("Podaj hasło:");
        user.setPassword(userInput.nextLine());
        System.out.println("Podaj id grupy:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        user.setGroupId(userInput.nextInt());
        return user;
    }

    public static User getUserDataToDelete() throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");

        System.out.println("Podaj id:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        User user = User.loadUserById(connection.getConnection(), userInput.nextInt());
        return user;
    }

}
