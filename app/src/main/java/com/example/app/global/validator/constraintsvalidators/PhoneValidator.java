package com.example.app.global.validator.constraintsvalidators;


import com.example.app.global.validator.constraints.Phone;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

	private static final String PATTERN = "\\d{3}-\\d{4}-\\d{4}";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}

		return Pattern.matches(PATTERN, value);
	}

}
