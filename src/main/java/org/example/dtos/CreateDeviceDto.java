package org.example.dtos;

import java.util.List;

public record CreateDeviceDto(String brandName, String deviceName, String deviceDescription, List<String> targetCountry,
                              DeviceConfigurationDto deviceConfiguration,
                              Integer value) {

    public record DeviceConfigurationDto(Integer minValue, Integer maxValue, Integer defaultValue) {
    }
}



















