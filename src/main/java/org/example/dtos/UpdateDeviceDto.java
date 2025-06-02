package org.example.dtos;

import java.util.List;

public record UpdateDeviceDto(Integer id, String brandName, String deviceName, String deviceDescription, List<String> targetCountry,
                              CreateDeviceDto.DeviceConfigurationDto deviceConfiguration) {
}
