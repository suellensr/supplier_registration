package edu.challengethree.supplier_registration.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DocumentValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDocument {
    String message() default "Invalid document";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
