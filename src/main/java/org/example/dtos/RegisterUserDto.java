package org.example.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(@Size(min = 3, max = 50) @NotBlank String username, @Size(max = 72, min = 3) String password,
                              @Size(max = 100) @NotBlank String name, @NotBlank String dob, @NotBlank String address,
                              @NotBlank @Size(min = 2, max = 2) String country) {
}
