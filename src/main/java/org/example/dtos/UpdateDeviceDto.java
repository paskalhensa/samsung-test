package org.example.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateDeviceDto(Integer id, @Size(max = 100) @NotBlank String brandName,
                              @Size(max = 150) @NotBlank String deviceName, String deviceDescription,
                              List<@NotBlank @Size(min = 2, max = 2) String> targetCountry,
                              @Valid @NotNull CreateDeviceDto.DeviceConfigurationDto deviceConfiguration) {
}
