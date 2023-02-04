package com.leesh.quiz.domain.quiz.validation;

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
@Constraint(validatedBy = QuizTitle.QuizTitleValidator.class)
@Documented
public @interface QuizTitle {

    String message() default "validation.quiz.title.default";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class QuizTitleValidator implements ConstraintValidator<QuizTitle, String> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            if (value == null || value.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        messageSource.getMessage("QUIZ.TITLE.NULL"))
                        .addConstraintViolation();

                isValid = false;
            }

            if (value != null && value.length() < 10) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("QUIZ.TITLE.MIN"))
                        .addConstraintViolation();

                isValid = false;
            }

            if (value != null && value.length() > 255) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("QUIZ.TITLE.MAX"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
