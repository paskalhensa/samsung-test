package org.example.dtos;

import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateDeviceDto(@Size(max = 100) String brandName, @Size(max = 150) String deviceName, String deviceDescription, List<String> targetCountry,
                              DeviceConfigurationDto deviceConfiguration) {

    public record DeviceConfigurationDto(Integer minValue, Integer maxValue, Integer defaultValue) {
    }
}



















