package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GroupsManagement {

    public static void main(String[] args) {

        try (DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"); Scanner userInput = new Scanner(System.in)) {
            boolean keepWorking = true;

            while (keepWorking) {
                ArrayList<Group> allGroups = Group.loadAllGroups(connection.getConnection());
                for (Group group : allGroups) {
                    System.out.println(group);
                }
                System.out.println();
                String userAction = getUserAction(userInput);

                if (userAction.equals("add")) {
                    Group group = getNewGroupData(userInput);
                    group.saveToDB(connection.getConnection());
                } else if (userAction.equals("edit")) {
                    Group group = getGroupDataToEdit(connection.getConnection(), userInput);
                    if (group != null) {
                        group.saveToDB(connection.getConnection());
                    } else {
                        System.out.println("Grupa o podanym id nie istnieje");
                    }
                } else if (userAction.equals("delete")) {
                    Group group = getGroupToDelete(connection.getConnection(), userInput);
                    if (group != null) {
                        group.delete(connection.getConnection());
                    } else {
                        System.out.println("Grupa o podanym id nie istnieje");
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
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie grupy,\nedit – edycja grupy,\ndelete – usunięcie grupy,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    private static Group getNewGroupData(Scanner userInput) {
        Group group = new Group();
        System.out.println("Podaj nazwę:");
        group.setGroupName(userInput.nextLine());
        return group;
    }

    private static Group getGroupDataToEdit(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Group group = Group.loadGroupById(connection, userInput.nextInt());
        if (group != null) {
            userInput.nextLine();
            System.out.println("Podaj nazwę:");
            group.setGroupName(userInput.nextLine());
            return group;
        } else {
            return null;
        }
    }

    private static Group getGroupToDelete(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        int id = userInput.nextInt();
        userInput.nextLine();
        return Group.loadGroupById(connection, id);
    }
}
