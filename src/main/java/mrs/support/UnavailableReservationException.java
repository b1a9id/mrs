package mrs.support;

import java.security.UnrecoverableEntryException;

public class UnavailableReservationException extends RuntimeException {

    public UnavailableReservationException(String messages) {
        super(messages);
    }
}
