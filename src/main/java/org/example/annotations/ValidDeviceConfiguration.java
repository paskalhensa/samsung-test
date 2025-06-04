package org.example.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.validators.DeviceConfigurationValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeviceConfigurationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDeviceConfiguration {
    String message() default "defaultValue must be between minValue and maxValue";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
