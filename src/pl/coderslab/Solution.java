package pl.coderslab;

import java.sql.*;
import java.util.ArrayList;

public class Solution {

    private int id;
    private Timestamp created;
    private Timestamp updated;
    private String description;
    private int exerciseId;
    private int userId;

    public Solution(int exerciseId, int userId) {
        this.exerciseId = exerciseId;
        this.userId = userId;
    }

    public Solution() {}

    public int getId() {
        return id;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            String sql = "insert into solutions(created, exercise_id, user_id) values(now(), ?, ?)";
            String generatedColumns[] = { "ID" };
            PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedColumns);
            preparedStatement.setInt(1, this.exerciseId);
            preparedStatement.setInt(2, this.userId);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            String sql = "update solutions set updated=now(), description=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.description);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }
    }

    public static Solution loadSolutionById(Connection connection, int id) throws SQLException {
        String sql = "select * from solutions where id =?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exerciseId = resultSet.getInt("exercise_id");
            loadedSolution.userId = resultSet.getInt("user_id");
            return loadedSolution;
        } else {
            return null;
        }

    }

    public static ArrayList<Solution> loadAllSolutions(Connection connection) throws SQLException {
        ArrayList<Solution> solutions = new ArrayList<>();
        String sql = "SELECT * FROM solutions";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        getSolutionData((ArrayList<Solution>) solutions, resultSet);
        return solutions;
    }

    public static ArrayList<Solution> loadAllByUserId(Connection connection, int userId) throws SQLException {
        ArrayList<Solution> solutions = new ArrayList<>();
        String sql = "select * from solutions where user_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        getSolutionData((ArrayList<Solution>) solutions, resultSet);
        return solutions;
    }

    public static ArrayList<Solution> loadAllByExerciseId(Connection connection, int exerciseId) throws SQLException {
        ArrayList<Solution> solutions = new ArrayList<>();
        String sql = "select * from solutions where exercise_id = ? order by updated desc";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, exerciseId);
        ResultSet resultSet = preparedStatement.executeQuery();
        getSolutionData((ArrayList<Solution>) solutions, resultSet);
        return solutions;
    }


    private static void getSolutionData(ArrayList<Solution> solutions, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Solution loadedSolution = new Solution();
            loadedSolution.id = resultSet.getInt("id");
            loadedSolution.created = resultSet.getTimestamp("created");
            loadedSolution.updated = resultSet.getTimestamp("updated");
            loadedSolution.description = resultSet.getString("description");
            loadedSolution.exerciseId = resultSet.getInt("exercise_id");
            loadedSolution.userId = resultSet.getInt("user_id");
            solutions.add(loadedSolution);
        }
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            String sql = "delete from solutions where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

    @Override
    public String toString() {
        return "id: " + id + ", created: " + created + ", updated: " + updated + ", description: " + description + ", exercise id: " + exerciseId + ", user id: " + userId;
    }


}
