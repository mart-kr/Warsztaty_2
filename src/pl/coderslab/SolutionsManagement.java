package pl.coderslab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class SolutionsManagement {

    public static void main(String[] args) {

        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");

        try {
            boolean keepWorking = true;

            while (keepWorking) {
                String userAction = getUserAction();

                if (userAction.equals("add")) {
                    Solution solution = getNewSolutionData();
                    solution.saveToDB(connection.getConnection());
                } else if (userAction.equals("view")) {
                    ArrayList<Solution> userSolutions = getUserSolutions();
                    for (Solution solution : userSolutions) {
                        System.out.println(solution);
                    }
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

        while (!userAction.equals("add") && !userAction.equals("view") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – przypisywanie zadań do użytkowników,\nview – przeglądanie rozwiązań danego użytkownika,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    public static Solution getNewSolutionData() throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");

        Solution solution = new Solution();
        ArrayList<User> allUsers = User.loadAllUsers(connection.getConnection());
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
        ArrayList<Exercise> allExercises = Exercise.loadAllExercises(connection.getConnection());
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

    public static ArrayList<Solution> getUserSolutions() throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab");

        System.out.println("Podaj id użytkownika:");
        Scanner userInput = new Scanner(System.in);
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        ArrayList<Solution> userSolutions = Solution.loadAllByUserId(connection.getConnection(), userInput.nextInt());
        return userSolutions;
    }
}
