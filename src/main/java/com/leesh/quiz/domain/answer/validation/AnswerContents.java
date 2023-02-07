package com.leesh.quiz.domain.answer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = AnswerContents.AnswerContentsValidator.class)
@Documented
public @interface AnswerContents {

    String message() default "ANSWER.CONTENTS.NULL";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class AnswerContentsValidator implements ConstraintValidator<AnswerContents, String> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            if (value == null || value.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        messageSource.getMessage("ANSWER.CONTENTS.NULL"))
                        .addConstraintViolation();

                isValid = false;
            }

            if (value != null && value.length() < 10) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("ANSWER.CONTENTS.MIN"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
