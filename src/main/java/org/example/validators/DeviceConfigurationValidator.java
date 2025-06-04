package org.example.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.annotations.ValidDeviceConfiguration;
import org.example.dtos.CreateDeviceDto;

public class DeviceConfigurationValidator implements ConstraintValidator<ValidDeviceConfiguration, CreateDeviceDto.DeviceConfigurationDto> {
    @Override
    public boolean isValid(CreateDeviceDto.DeviceConfigurationDto config, ConstraintValidatorContext constraintValidatorContext) {
        if (config == null) return true;
        Integer min = config.minValue();
        Integer max = config.maxValue();
        Integer def = config.defaultValue();
        if (min == null || max == null || def == null) return true;
        if (min > max || def < min || def > max) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("defaultValue must be between minValue and maxValue")
                    .addPropertyNode("defaultValue").addConstraintViolation();
            return false;
        }
        return true;
    }
}
