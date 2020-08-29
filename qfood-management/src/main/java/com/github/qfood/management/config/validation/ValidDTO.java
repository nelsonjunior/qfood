package com.github.qfood.management.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidDTOValidator.class})
@Documented
public @interface ValidDTO {

    String message() default "{com.github.qfood.management.dto.ValidDTO.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}