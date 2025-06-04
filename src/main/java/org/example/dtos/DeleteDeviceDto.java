package org.example.dtos;

import jakarta.validation.constraints.NotNull;

public record DeleteDeviceDto(@NotNull Integer deviceId) {
}
