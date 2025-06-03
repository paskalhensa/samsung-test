package org.example.dtos;

import java.util.List;

public record ResponseDto(boolean success, String message, Object data, List<String> errors) {
}
