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
@Constraint(validatedBy = UserPassword.UserPasswordValidator.class)
@Documented
public @interface UserPassword {

    String message() default "validation.user.password.default";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            if (value == null || value.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        messageSource.getMessage("validation.user.password.null"))
                        .addConstraintViolation();

                isValid = false;
            }

            // 모든 한글 음절, 영어 대/소문자, 숫자만 가능
            Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\\\d)(?=.*\\\\W).{8,30}$");
            if (value != null && !pattern.matcher(value).matches()) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("validation.user.password.invalid"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
