package mrs.app.reservation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@EndTimeMustBeAfterStartTime
public class ReservationForm implements Serializable {

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @ThirtyMinutesUnit
    private LocalTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    @ThirtyMinutesUnit
    private LocalTime endTime;
}
