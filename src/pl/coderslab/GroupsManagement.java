package pl.coderslab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GroupsManagement {

    public static void main(String[] args) {

        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");

        try {
            boolean keepWorking = true;

            while (keepWorking) {
                ArrayList<Group> allGroups = Group.loadAllGroups(connection.getConnection());
                for (Group group : allGroups) {
                    System.out.println(group);
                }
                System.out.println();
                String userAction = getUserAction();

                if (userAction.equals("add")) {
                    Group group = getNewGroupData();
                    group.saveToDB(connection.getConnection());
                } else if (userAction.equals("edit")) {
                    Group group = getGroupDataToEdit();
                    group.saveToDB(connection.getConnection());
                } else if (userAction.equals("delete")) {
                    Group group = getGroupToDelete();
                    group.delete(connection.getConnection());
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
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie grupy,\nedit – edycja grupy,\ndelete – usunięcie grupy,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    public static Group getNewGroupData() {
        Group group = new Group();
        System.out.println("Podaj nazwę:");
        Scanner userInput = new Scanner(System.in);
        group.setGroupName(userInput.nextLine());
        return group;
    }

    public static Group getGroupDataToEdit() throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");

        System.out.println("Podaj id:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Group group = Group.loadGroupById(connection.getConnection(), userInput.nextInt());
        userInput.nextLine();
        System.out.println("Podaj nazwę:");
        group.setGroupName(userInput.nextLine());
        return group;
    }

    public static Group getGroupToDelete() throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");

        System.out.println("Podaj id:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Group group = Group.loadGroupById(connection.getConnection(), userInput.nextInt());
        return group;
    }
}
