package mrs.app.reservation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { ThirtyMinutesUnitValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThirtyMinutesUnit {
    String message() default "{mrs.app.reservation.ThirtyMinutesUnit.message}";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};

    @Target({ElementType. METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        ThirtyMinutesUnit[]value();
    }
}
