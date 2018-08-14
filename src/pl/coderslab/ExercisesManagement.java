package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExercisesManagement {

    public static void main(String[] args) {

        try (DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab"); Scanner userInput = new Scanner(System.in)) {
            boolean keepWorking = true;

            while (keepWorking) {
                ArrayList<Exercise> allExercises = Exercise.loadAllExercises(connection.getConnection());
                for (Exercise exercise : allExercises) {
                    System.out.println(exercise);
                }
                System.out.println();
                String userAction = getUserAction(userInput);

                if (userAction.equals("add")) {
                    Exercise exercise = getNewExerciseData(userInput);
                    exercise.saveToDB(connection.getConnection());
                } else if (userAction.equals("edit")) {
                    Exercise exercise = getExerciseDataToEdit(connection.getConnection(), userInput);
                    if (exercise !=null) {
                        exercise.saveToDB(connection.getConnection());
                    } else {
                        System.out.println("Zadanie o podanym id nie istnieje.");
                    }
                } else if (userAction.equals("delete")) {
                    Exercise exercise = getExerciseToDelete(connection.getConnection(), userInput);
                    if (exercise != null) {
                        exercise.delete(connection.getConnection());
                    } else {
                        System.out.println("Zadanie o podanym id nie istnieje.");
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
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie zadania,\nedit – edycja zadania,\ndelete – usunięcie zadania,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    private static Exercise getNewExerciseData(Scanner userInput) {
        Exercise exercise = new Exercise();
        System.out.println("Podaj tytuł:");
        exercise.setTitle(userInput.nextLine());
        System.out.println("Podaj opis:");
        exercise.setDescription(userInput.nextLine());
        return exercise;
    }

    private static Exercise getExerciseDataToEdit(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Exercise exercise = Exercise.loadExerciseById(connection, userInput.nextInt());
        if (exercise != null) {
            userInput.nextLine();
            System.out.println("Podaj tytuł:");
            exercise.setTitle(userInput.nextLine());
            System.out.println("Podaj opis:");
            exercise.setDescription(userInput.nextLine());
            return exercise;
        } else {
            return null;
        }

    }

    private static Exercise getExerciseToDelete(Connection connection, Scanner userInput) throws SQLException {
        System.out.println("Podaj id:");

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        int id = userInput.nextInt();
        userInput.nextLine();
        return Exercise.loadExerciseById(connection, id);
    }

}
