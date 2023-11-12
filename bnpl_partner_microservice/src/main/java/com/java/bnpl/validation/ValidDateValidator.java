package com.java.bnpl.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       
        if (value == null || value.isBlank()) {
            return true;  // Null values are handled by @NotNull or @NotBlank
        }
        try{
             System.out.println("***** I SHOULD PRINT ******");
            LocalDate dt = LocalDate.parse(value);
            return true; 
        }catch(Exception e){
            System.out.println("***** I SHOULD FAILED ******");
            return false; 
        }
       
    }
}
