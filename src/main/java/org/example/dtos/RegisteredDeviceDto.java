package org.example.dtos;

public record RegisteredDeviceDto(Integer userDeviceId, Integer deviceId, String brandName, String deviceName, String deviceDescription, CreateDeviceDto.DeviceConfigurationDto deviceConfiguration, Integer value) {
}
