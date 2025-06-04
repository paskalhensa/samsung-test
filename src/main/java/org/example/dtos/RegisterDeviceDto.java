package org.example.dtos;

import jakarta.validation.constraints.NotNull;

public record RegisterDeviceDto(@NotNull Integer deviceId) {
}
