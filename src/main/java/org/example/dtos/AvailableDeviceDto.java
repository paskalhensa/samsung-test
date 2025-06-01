package org.example.dtos;

public record AvailableDeviceDto(String brandName, String deviceName, String deviceDescription, CreateDeviceDto.DeviceConfigurationDto deviceConfiguration) {
}
