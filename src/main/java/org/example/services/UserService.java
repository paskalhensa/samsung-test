package org.example.services;

import org.example.dtos.RegisterUserDto;
import org.example.dtos.UsersDto;
import org.example.utils.DataSourceProvider;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserService {
    public UsersDto findByUsername(String username) throws SQLException {
        String script = "SELECT id, username, password_hash, role FROM users WHERE username = ?";
        try(Connection connection = DataSourceProvider.getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(script)){
            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){
                    return new UsersDto(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("role"));
                } else {
                    return null;
                }
            }
        }
    }

    public void createUser(RegisterUserDto user) throws SQLException {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer userId = insertUser(connection, user);
                insertUserProfile(connection, user, userId);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private void insertUserProfile(Connection connection, RegisterUserDto user, Integer userId) throws SQLException {
        String script = "INSERT INTO smartthings_user_profiles (user_id, full_name, dob, user_address, country_code) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, userId);
            statement.setString(2, user.name());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDate.parse(user.dob(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()));
            statement.setString(4, user.address());
            statement.setString(5, user.country());
            statement.executeUpdate();
        }
    }

    private Integer insertUser(Connection connection, RegisterUserDto user) throws SQLException {
        String script = "INSERT INTO users (username, password_hash, role, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.username());
            statement.setString(2, BCrypt.hashpw(user.password(), BCrypt.gensalt()));
            statement.setString(3, "client");
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve user Id");
                }
            }
        }
    }
}
