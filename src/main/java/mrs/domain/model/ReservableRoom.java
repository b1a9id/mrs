package mrs.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class ReservableRoom implements Serializable {

    @EmbeddedId
    private ReservableRoomId reservableId;

    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    @MapsId("roomId")
    private MeetingRoom meetingRoom;

    public ReservableRoom(ReservableRoomId reservableId) {
        this.reservableId = reservableId;
    }

    public ReservableRoom() {}
}
