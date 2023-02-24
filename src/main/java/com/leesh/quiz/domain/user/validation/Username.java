package com.leesh.quiz.domain.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = Username.UsernameValidator.class)
@Documented
public @interface Username {

    String message() default "USER.USERNAME.NULL";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class UsernameValidator implements ConstraintValidator<Username, String> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            if (!StringUtils.hasText(value)) {
                context.buildConstraintViolationWithTemplate(messageSource.getMessage("USER.USERNAME.NULL"))
                        .addConstraintViolation();
                isValid = false;
            }

            if (value.length() < 2 || value.length() > 20) {
                context.buildConstraintViolationWithTemplate(messageSource.getMessage("USER.USERNAME.LENGTH"))
                        .addConstraintViolation();
                isValid = false;
            }

            return isValid;
        }
    }

}
