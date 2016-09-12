package mrs.app.reservation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EndTimeMustBeAfterStartTimeValidator.class})
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndTimeMustBeAfterStartTime {

    String message() default "{mrs.appreservation.EndTimeMustBeAfterStartTime.message}";

    Class<?>[]groups() default {};

    Class<? extends Payload>[]payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        EndTimeMustBeAfterStartTime[]value();
    }
}
