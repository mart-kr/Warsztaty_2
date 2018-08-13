package pl.coderslab;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExercisesManagement {

    public static void main(String[] args) {

        try (DBConnection connection = new DBConnection("jdbc:mysql://localhost:3306/programming_school?useSSL=false&characterEncoding=utf8",
                "root",
                "coderslab")) {
            boolean keepWorking = true;

            while (keepWorking) {
                ArrayList<Exercise> allExercises = Exercise.loadAllExercises(connection.getConnection());
                for (Exercise exercise : allExercises) {
                    System.out.println(exercise);
                }
                System.out.println();
                String userAction = getUserAction();

                if (userAction.equals("add")) {
                    Exercise exercise = getNewExerciseData();
                    exercise.saveToDB(connection.getConnection());
                } else if (userAction.equals("edit")) {
                    Exercise exercise = getExerciseDataToEdit(connection.getConnection());
                    exercise.saveToDB(connection.getConnection());
                } else if (userAction.equals("delete")) {
                    Exercise exercise = getExerciseToDelete(connection.getConnection());
                    exercise.delete(connection.getConnection());
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

        while (!userAction.equals("add") && !userAction.equals("edit") && !userAction.equals("delete") && !userAction.equals("quit")) {
            System.out.println("Wybierz jedną z opcji:\n\nadd – dodanie zadania,\nedit – edycja zadania,\ndelete – usunięcie zadania,\nquit – zakończenie programu.");
            userAction = userInput.nextLine();
        }
        return userAction;
    }

    public static Exercise getNewExerciseData() {
        Exercise exercise = new Exercise();
        System.out.println("Podaj tytuł:");
        Scanner userInput = new Scanner(System.in);
        exercise.setTitle(userInput.nextLine());
        System.out.println("Podaj opis:");
        exercise.setDescription(userInput.nextLine());
        return exercise;
    }

    public static Exercise getExerciseDataToEdit(Connection connection) throws SQLException {
        System.out.println("Podaj id:");
        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Exercise exercise = Exercise.loadExerciseById(connection, userInput.nextInt());
        userInput.nextLine();
        System.out.println("Podaj tytuł:");
        exercise.setTitle(userInput.nextLine());
        System.out.println("Podaj opis:");
        exercise.setDescription(userInput.nextLine());
        return exercise;
    }

    public static Exercise getExerciseToDelete(Connection connection) throws SQLException {
        System.out.println("Podaj id:");
        Scanner userInput = new Scanner((System.in));

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Podaj id!");
        }
        Exercise exercise = Exercise.loadExerciseById(connection, userInput.nextInt());
        return exercise;
    }


}
