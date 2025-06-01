package org.example.dtos;

public record RegisterUserDto(String username, String password, String name, String dob, String address, String country) {
}
