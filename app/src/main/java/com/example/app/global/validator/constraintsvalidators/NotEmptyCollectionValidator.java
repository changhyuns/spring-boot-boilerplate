package com.example.app.global.validator.constraintsvalidators;

import com.example.app.global.validator.constraints.NotEmptyCollection;
import java.util.Collection;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyCollectionValidator implements ConstraintValidator<NotEmptyCollection, Collection<Long>> {

	@Override
	public boolean isValid(Collection<Long> objects, ConstraintValidatorContext context) {
		if (objects.isEmpty()) {
			return false;
		}

		return objects.stream().allMatch(Objects::nonNull);
	}

}
