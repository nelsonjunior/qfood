package com.github.qfood.management.domain.dto;

import com.github.qfood.management.domain.entity.Restaurant;
import com.github.qfood.management.config.validation.ValidDTO;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidDTO
public class AddRestaurantDTO implements BaseDTO {

    @NotEmpty
    @NotNull
    public String owner;

    @Pattern(regexp = "[0-9]{2}\\.[0-9]{3}\\.[0-9]{3}\\/[0-9]{4}\\-[0-9]{2}")
    @NotNull
    public String documentID;

    @NotEmpty
    @NotNull
    @Size(min = 3, max = 50)
    public String name;

    public LocationDTO location;

    @Override
    public boolean isValid(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        if (Restaurant.find("documentID", documentID).count() > 0) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("DocumentID already exists")
                    .addPropertyNode("documentID")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
