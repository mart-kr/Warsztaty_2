package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolutionsManagement {

    public static void main(String[] args) {

        try (DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab")) {
            boolean keepWorking = true;

            while (keepWorking) {
                String userAction = getUserAction();

                if (userAction.equals("add")) {
                    Solution solution = getNewSolutionData(connection.getConnection());
                    solution.saveToDB(connection.getConnection());
                } else if (userAction.equals("view")) {
                    ArrayList<Solution> userSolutions = getUserSolutions(connection.getConnection());
                    for (Solution solution : userSolutions) {
                        System.out.println(solution);
                    }
                } else {
                    keepWorking = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserAction() {
        Scanner userInput = new Scanner(System.in);
        String userAction = "";

        while (!userAction.equals("add") && !userAction.equals("view") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – przypisywanie zadań do użytkowników,\nview – przeglądanie rozwiązań danego użytkownika,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    public static Solution getNewSolutionData(Connection connection) throws SQLException {
        Solution solution = new Solution();
        ArrayList<User> allUsers = User.loadAllUsers(connection);
        for (User user : allUsers) {
            System.out.println(user);
        }
        System.out.println();
        System.out.println("Podaj id użytkownika:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        solution.setUserId(userInput.nextInt());
        userInput.nextLine();
        ArrayList<Exercise> allExercises = Exercise.loadAllExercises(connection);
        for (Exercise exercise : allExercises) {
            System.out.println(exercise);
        }
        System.out.println();
        System.out.println("Podaj id zadania:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        solution.setExerciseId(userInput.nextInt());
        return solution;
    }

    public static ArrayList<Solution> getUserSolutions(Connection connection) throws SQLException {
        System.out.println("Podaj id użytkownika:");
        Scanner userInput = new Scanner(System.in);
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        ArrayList<Solution> userSolutions = Solution.loadAllByUserId(connection, userInput.nextInt());
        return userSolutions;
    }
}
