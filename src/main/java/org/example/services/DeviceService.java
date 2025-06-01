package org.example.services;

import org.example.dtos.AvailableDeviceDto;
import org.example.dtos.CreateDeviceDto;
import org.example.dtos.GetDeviceDto;
import org.example.utils.DataSourceProvider;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeviceService {

    public void createDevice(CreateDeviceDto device, Integer userId) throws SQLException {
        try (Connection connection = DataSourceProvider.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer deviceId = insertDevice(connection, device, userId);
                for (String country : device.targetCountry()) {
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
        try (PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setString(1, country);
            statement.setInt(2, deviceId);
            statement.executeUpdate();
        }
    }

    private Integer insertDevice(Connection connection, CreateDeviceDto device, Integer userId) throws SQLException {
        String script = "INSERT INTO devices (brand_name, device_name, device_description, min_value, max_value, default_value, created_at, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, device.brandName());
            statement.setString(2, device.deviceName());
            statement.setString(3, device.deviceDescription());
            statement.setInt(4, device.deviceConfiguration().minValue());
            statement.setInt(5, device.deviceConfiguration().maxValue());
            statement.setInt(6, device.deviceConfiguration().defaultValue());
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(8, userId);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve device Id");
                }
            }
        }
    }

    public List<GetDeviceDto> getDevice(Integer id) throws SQLException {
        String script = "SELECT brand_name, device_name, device_description FROM devices WHERE user_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, id);
            List<GetDeviceDto> devices = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    devices.add(new GetDeviceDto(
                            resultSet.getString("brand_name"),
                            resultSet.getString("device_name"),
                            resultSet.getString("device_description")
                    ));
                }
                return devices;
            }
        }
    }

    public List<AvailableDeviceDto> getAvailableDevice(Integer id) throws SQLException {
        String script = "SELECT d.id, brand_name, device_name, device_description, min_value, max_value, default_value FROM " +
                "devices d JOIN device_target_countries dtc ON d.id = dtc.device_id " +
                "JOIN smartthings_user_profiles sup ON sup.country_code = dtc.country_code WHERE sup.user_id = ?";
        try (Connection connection = DataSourceProvider.getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, id);
            List<AvailableDeviceDto> devices = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    devices.add(new AvailableDeviceDto(
                            resultSet.getInt("id"),
                            resultSet.getString("brand_name"),
                            resultSet.getString("device_name"),
                            resultSet.getString("device_description"),
                            new CreateDeviceDto.DeviceConfigurationDto(
                                    resultSet.getInt("min_value"),
                                    resultSet.getInt("max_value"),
                                    resultSet.getInt("default_value"))
                    ));
                }
                return devices;
            }
        }
    }
}
