package org.example.services;

import org.example.dtos.UsersDto;
import org.example.utils.DataSourceProvider;

import java.sql.*;

public class UserService {
    public UsersDto findByUsername(String username) throws SQLException {
        String script = "SELECT username, password_hash, role FROM users WHERE username = ?";
        try(Connection connection = DataSourceProvider.getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(script)){
            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){
                    return new UsersDto(
                            resultSet.getString("username"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("role"));
                } else {
                    return null;
                }
            }
        }
    }

}
