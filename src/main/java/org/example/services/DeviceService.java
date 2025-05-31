package org.example.services;

import org.example.config.DbConfig;
import org.example.dtos.CreateDeviceDto;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeviceService {
    private final DbConfig dbConfig;

    @Inject
    public DeviceService(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public void createDevice(CreateDeviceDto device) throws SQLException {
        String script = "INSERT INTO devices (brand_name, device_name, device_description, min_value, max_value, default_value) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(script)){
            statement.setString(1, device.getBrandName());
            statement.setString(2, device.getDeviceName());
            statement.setString(3, device.getDeviceDescription());
            statement.setInt(4, device.getDeviceConfiguration().getMinValue());
            statement.setInt(5, device.getDeviceConfiguration().getMaxValue());
            statement.setInt(6, device.getDeviceConfiguration().getDefaultValue());
            statement.executeUpdate();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConfig.url(), dbConfig.username(), dbConfig.password());
    }
}
