package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolutionsManagement {

    public static void main(String[] args) {

        try (DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"); Scanner userInput = new Scanner(System.in)) {
            boolean keepWorking = true;

            while (keepWorking) {
                String userAction = getUserAction(userInput);

                if (userAction.equals("add")) {
                    Solution solution = getNewSolutionData(connection.getConnection(), userInput);
                    if (solution != null) {
                        solution.saveToDB(connection.getConnection());
                    } else {
                        System.out.println("Zadanie jest już przypisane do użytkownika.");
                    }
                } else if (userAction.equals("view")) {
                    ArrayList<Solution> userSolutions = getUserSolutions(connection.getConnection(), userInput);
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

    private static String getUserAction(Scanner userInput) {
        String userAction = "";

        while (!userAction.equals("add") && !userAction.equals("view") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – przypisywanie zadań do użytkowników,\nview – przeglądanie rozwiązań danego użytkownika,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    private static Solution getNewSolutionData(Connection connection, Scanner userInput) throws SQLException {
        Solution solution = new Solution();
        ArrayList<User> allUsers = User.loadAllUsers(connection);
        for (User user : allUsers) {
            System.out.println(user);
        }
        System.out.println();
        int userId = 0;
        while (solution.getUserId() == 0) {
            System.out.println("Podaj id użytkownika:");
            while (!userInput.hasNextInt()) {
                userInput.next();
                System.out.println("Podaj id!");
            }
            userId = userInput.nextInt();
            if (User.loadUserById(connection, userId) != null) {
                solution.setUserId(userId);
            } else {
                System.out.println("Użytkownik o podanym id nie istnieje.");
            }
        }
        userInput.nextLine();
        ArrayList<Exercise> allExercises = Exercise.loadAllExercises(connection);
        for (Exercise exercise : allExercises) {
            System.out.println(exercise);
        }
        System.out.println();
        int exerciseId = 0;
        while (solution.getExerciseId() == 0) {
            System.out.println("Podaj id zadania:");

            while (!userInput.hasNextInt()) {
                userInput.next();
                System.out.println("Podaj id!");
            }
            exerciseId = userInput.nextInt();
            if (Exercise.loadExerciseById(connection, exerciseId) != null) {
                solution.setExerciseId(exerciseId);
            } else {
                System.out.println("Zadanie o podanym id nie istnieje.");
            }
        }
        if (Solution.loadSolutionByExerciseAndUser(connection, exerciseId, userId) == null) {
            userInput.nextLine();
            return solution;
        } else {
            return null;
        }
    }

    private static ArrayList<Solution> getUserSolutions(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id użytkownika:");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        int userId = userInput.nextInt();
        userInput.nextLine();
        return Solution.loadAllByUserId(connection, userId);
    }
}
