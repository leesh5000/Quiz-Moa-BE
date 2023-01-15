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
@Constraint(validatedBy = UserNickname.UserNicknameValidator.class)
@Documented
public @interface UserNickname {

    String message() default "validation.user.nickname.default";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class UserNicknameValidator implements ConstraintValidator<UserNickname, String> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            if (value == null || value.isBlank()) {
                context.buildConstraintViolationWithTemplate(
                        messageSource.getMessage("validation.user.nickname.null"))
                        .addConstraintViolation();

                isValid = false;
            }

            if (value != null && value.length() > 30) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("validation.user.nickname.size"))
                        .addConstraintViolation();

                isValid = false;
            }

            // 모든 한글 음절, 영어 대/소문자, 숫자만 가능
            Pattern pattern = Pattern.compile("^\uAC00-\uD7A30-9a-zA-Z\\s");
            if (value != null && !pattern.matcher(value).matches()) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("validation.user.nickname.invalid"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
