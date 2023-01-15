package com.leesh.quiz.domain.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UserEmail.UserEmailValidator.class)
@Documented
public @interface UserEmail {

    String message() default "validation.user.email.default";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class UserEmailValidator implements ConstraintValidator<UserEmail, String> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            if (value == null || value.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        messageSource.getMessage("validation.user.email.null"))
                        .addConstraintViolation();

                isValid = false;
            }

            if (value != null && value.length() > 255) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("validation.user.email.size"))
                        .addConstraintViolation();

                isValid = false;
            }

            Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            if (value != null && !pattern.matcher(value).matches()) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("validation.user.email.invalid"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
