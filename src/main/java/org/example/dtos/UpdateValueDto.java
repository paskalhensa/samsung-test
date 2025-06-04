package org.example.dtos;

import jakarta.validation.constraints.NotNull;

public record UpdateValueDto(@NotNull Integer userDeviceId, @NotNull Integer value) {
}
