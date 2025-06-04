package org.example.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

public class ValidatorUtil {
    private static final Validator validator;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public static <T> List<String> validate(T object){
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        return violations.stream().map(v -> "%s: %s".formatted(v.getPropertyPath(), v.getMessage())).toList();
    }
}
