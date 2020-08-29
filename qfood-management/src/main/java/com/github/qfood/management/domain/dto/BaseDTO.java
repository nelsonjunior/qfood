package com.github.qfood.management.domain.dto;

import javax.validation.ConstraintValidatorContext;

public interface BaseDTO {
    default boolean isValid(ConstraintValidatorContext constraintValidatorContext){
        return true;
    }
}
