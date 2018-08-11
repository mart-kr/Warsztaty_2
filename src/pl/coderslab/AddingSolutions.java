package pl.coderslab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AddingSolutions {

    public static void main(String[] args) {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");
        try {
            int userId = Integer.valueOf(args[0]);
            boolean keepWorking = true;

            while (keepWorking) {
                String userAction = getUserAction();

                if (userAction.equals("add")) {
                    System.out.println("Zadania bez rozwiązań:");
                    ArrayList<Solution> userUnsolvedItems = Solution.loadUnsolvedByUserId(connection.getConnection(), userId);
                    for (Solution solution : userUnsolvedItems) {
                        Exercise exercise = Exercise.loadExerciseById(connection.getConnection(), solution.getExerciseId());
                        System.out.println(exercise);
                    }
                    Solution solutionToEdit = getSolutionToEdit(userId);
                    solutionToEdit.saveToDB(connection.getConnection());
                } else if (userAction.equals("view")) {
                    ArrayList<Solution> userSolutions = Solution.loadAllByUserId(connection.getConnection(), userId);
                    for (Solution userSolution : userSolutions) {
                        System.out.println(userSolution);
                    }
                } else {
                    keepWorking = false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (InputMismatchException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserAction() {
        Scanner userInput = new Scanner(System.in);
        String userAction = "";

        while (!userAction.equals("add") && !userAction.equals("view") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie użytkownika,\nview – przeglądanie swoich rozwiązań,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    public static Solution getSolutionToEdit(int userId) throws SQLException {
        DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");

        System.out.println("Podaj id:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Solution solution = Solution.loadSolutionByExerciseAndUser(connection.getConnection(), userInput.nextInt(), userId);
        userInput.nextLine();
        System.out.println("Podaj rozwiązanie:");
        solution.setDescription(userInput.nextLine()); //TODO: spróbować przerobić, żeby przyjmować więcej linii
        return solution;
    }
}
