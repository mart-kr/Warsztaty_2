package pl.coderslab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group {

    private int id;
    private String groupName;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public Group() {}

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            String sql = "insert into user_groups(name) values(?)";
            String generatedColumns[] = { "ID" };
            PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.groupName);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            String sql = "update user_groups set name=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.groupName);
            preparedStatement.setInt(2, this.id);
            preparedStatement.executeUpdate();
        }
    }

    public static Group loadGroupById(Connection connection, int id) throws SQLException {
        String sql = "select * from user_groups where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Group loadedGroup = new Group();
            loadedGroup.id = resultSet.getInt("id");
            loadedGroup.groupName = resultSet.getString("name");
            return loadedGroup;
        } else {
            return null;
        }
    }

    public static ArrayList<Group> loadAllGroups(Connection connection) throws SQLException {
        ArrayList<Group> groups = new ArrayList<>();
        String sql = "select * from user_groups";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Group loadedGroup = new Group();
            loadedGroup.id = resultSet.getInt("id");
            loadedGroup.groupName = resultSet.getString("name");
            groups.add(loadedGroup);
        }
        return groups;
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            String sql = "delete from user_groups where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + groupName;
    }


}

