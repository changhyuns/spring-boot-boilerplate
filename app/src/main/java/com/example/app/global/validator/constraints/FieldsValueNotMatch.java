package com.example.app.global.validator.constraints;

import com.example.app.global.validator.constraintsvalidators.FieldsValueNotMatchValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FieldsValueNotMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldsValueNotMatch {

	String message() default "validation.password.not.change";

	String field();

	String fieldMatch();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
