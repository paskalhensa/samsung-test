package org.example.dtos;

public record GetUserDeviceDto(String username, String fullName, Integer registeredDeviceCount) {
}
