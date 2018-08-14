package pl.coderslab;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private int id;
    private String userName;
    private String email;
    private String password;
    private int groupId;

    public User(String userName, String email, String password, int groupId) {
        this.userName = userName;
        this.email = email;
        this.setPassword(password);
        this.groupId = groupId;
    }

    public User() {}

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            String sql = "insert into users(user_name, email, password, user_group_id) values(?,?,?,?)";
            String generatedColumns[] = { "ID" };
            PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setInt(4, this.groupId);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                this.id = resultSet.getInt(1);
            }
        } else {
            String sql = "update users set user_name=?, email=?, password=?, user_group_id=?  where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, this.email);
            preparedStatement.setString(3, this.password);
            preparedStatement.setInt(4, this.groupId);
            preparedStatement.setInt(5, this.id);
            preparedStatement.executeUpdate();
        }

    }

    public static User loadUserById(Connection connection, int id) throws SQLException {
        String sql = "select * from users where id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            User loadedUser = new User();
            loadedUser.id = resultSet.getInt("id");
            loadedUser.userName = resultSet.getString("user_name");
            loadedUser.password = resultSet.getString("password");
            loadedUser.email = resultSet.getString("email");
            loadedUser.groupId = resultSet.getInt("user_group_id");
            return loadedUser;
        } else {
            return null;
        }
    }

    public static ArrayList<User> loadAllUsers(Connection connection) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        getUserData(users, resultSet);
        return users;
    }

    public static ArrayList<User> loadAllByGroupId(Connection connection, int groupId) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users where user_group_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, groupId);
        ResultSet resultSet = preparedStatement.executeQuery();
        getUserData(users, resultSet);
        return users;
    }

    private static void getUserData(ArrayList<User> users, ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            User loadedUser = new User();
            loadedUser.id = resultSet.getInt("id");
            loadedUser.userName = resultSet.getString("user_name");
            loadedUser.password = resultSet.getString("password");
            loadedUser.email = resultSet.getString("email");
            loadedUser.groupId = resultSet.getInt("user_group_id");
            users.add(loadedUser);
        }
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            String sql = "delete from users where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.id);
            preparedStatement.executeUpdate();
            this.id = 0;
        }
    }

    @Override
    public String toString() {
        return "id: " + id + ", imię: " + userName + ", email: " + email + ", id grupy: " + groupId;
    }
}
