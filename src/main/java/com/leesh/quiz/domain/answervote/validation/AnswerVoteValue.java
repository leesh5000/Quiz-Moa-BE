package com.leesh.quiz.domain.answervote.validation;

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
@Constraint(validatedBy = AnswerVoteValue.AnswerVoteValidator.class)
@Documented
public @interface AnswerVoteValue {

    String message() default "ANSWERVOTE.INVALID.VALUE";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class AnswerVoteValidator implements ConstraintValidator<AnswerVoteValue, Byte> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(Byte value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            // 투표 값은 항상 1 이거나 -1 이어야 함
            if (value == null || !(value == 1 || value == -1)) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("ANSWERVOTE.INVALID.VALUE"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
