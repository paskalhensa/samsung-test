package org.example.dtos;

import jakarta.validation.constraints.NotNull;

public record UnregisterDeviceDto(@NotNull Integer userDeviceId) {
}
