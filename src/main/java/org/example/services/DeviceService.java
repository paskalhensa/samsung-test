package org.example.services;

import org.example.dtos.CreateDeviceDto;
import org.example.utils.DataSourceProvider;

import java.sql.*;
import java.time.LocalDateTime;

public class DeviceService {

    public void createDevice(CreateDeviceDto device) throws SQLException {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer deviceId = insertDevice(connection, device);
                for(String country : device.targetCountry()){
                    insertTargetCountry(connection, deviceId, country);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private void insertTargetCountry(Connection connection, Integer deviceId, String country) throws SQLException {
        String script = "INSERT INTO device_target_countries (country_code, device_id) VALUES (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(script)){
            statement.setString(1, country);
            statement.setInt(2, deviceId);
            statement.executeUpdate();
        }
    }

    private Integer insertDevice(Connection connection, CreateDeviceDto device) throws SQLException {
        String script = "INSERT INTO devices (brand_name, device_name, device_description, min_value, max_value, default_value, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, device.brandName());
            statement.setString(2, device.deviceName());
            statement.setString(3, device.deviceDescription());
            statement.setInt(4, device.deviceConfiguration().minValue());
            statement.setInt(5, device.deviceConfiguration().maxValue());
            statement.setInt(6, device.deviceConfiguration().defaultValue());
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if(resultSet.next()){
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve device Id");
                }
            }
        }
    }

}
