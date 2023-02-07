package com.leesh.quiz.domain.quizvote.validation;

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
@Constraint(validatedBy = QuizVoteValue.QuizVoteValidator.class)
@Documented
public @interface QuizVoteValue {

    String message() default "validation.quiz.title.default";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    @RequiredArgsConstructor
    class QuizVoteValidator implements ConstraintValidator<QuizVoteValue, Byte> {

        private final MessageSourceAccessor messageSource;

        @Override
        public boolean isValid(Byte value, ConstraintValidatorContext context) {

            context.disableDefaultConstraintViolation();
            boolean isValid = true;

            // 투표 값은 항상 1 이거나 -1 이어야 함
            if (value == null || !(value == 1 || value == -1)) {
                context.buildConstraintViolationWithTemplate(
                                messageSource.getMessage("QUIZVOTE.INVALID.VALUE"))
                        .addConstraintViolation();

                isValid = false;
            }

            return isValid;
        }
    }

}
