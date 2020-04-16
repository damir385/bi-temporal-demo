package com.example.persistence.api.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = TimeConsistencyValidator.class)
public @interface CheckTimeConsistency {

    //TODO define proper messages
    String message() default "Time consistency validation failed";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };


}
