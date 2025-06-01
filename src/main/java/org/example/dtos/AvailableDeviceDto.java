package org.example.dtos;

public record AvailableDeviceDto(Integer id, String brandName, String deviceName, String deviceDescription, CreateDeviceDto.DeviceConfigurationDto deviceConfiguration) {
}
