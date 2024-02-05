package com.example.beautybook.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageFile {
    String message() default "Invalid value. Supported values are: {enumClass.simpleName}";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
