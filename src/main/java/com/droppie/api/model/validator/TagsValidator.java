package com.droppie.api.model.validator;

import com.droppie.api.annotation.TagConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TagsValidator implements ConstraintValidator<TagConstraint, List<String>> {
    @Override
    public boolean isValid(List<String> tags, ConstraintValidatorContext context) {
        return tags
                .stream()
                .filter(this::validateTag)
                .count() < 1;
    }

    private Boolean validateTag(String tag) {
        return tag.isEmpty() || !tag.matches("^[a-z0-9]*$");
    }
}

