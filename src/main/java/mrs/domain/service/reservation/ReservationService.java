package mrs.domain.service.reservation;

import mrs.domain.model.*;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.ReservableRoomRepository;
import mrs.support.AlreadyReservedExceptiom;
import mrs.support.UnavailableReservationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservableRoomRepository reservableRoomRepository;

    public Reservation reserve(Reservation reservation) {
        ReservableRoomId reservableRoomId = reservation.getReservableRoom().getReservableRoomId();
        ReservableRoom reservableRoom = reservableRoomRepository.findOneForUpdateByReservableRoomId(reservableRoomId);
        if (reservableRoom == null) {
            throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません。");
        }
        boolean overlap = reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId)
                .stream()
                .anyMatch(target -> target.overlap(reservation));
        if (overlap) {
            throw new AlreadyReservedExceptiom("入力の時間帯はすでに予約済みです。");
        }
        return reservationRepository.save(reservation);
    }

    public void cancel(Integer reservationId, User requestUser) {
        Reservation reservation = reservationRepository.findOne(reservationId);
        if (Objects.isNull(reservation)) {
            throw new IllegalStateException("予約が存在しません。v");
        }
        if (RoleName.ADMIN != requestUser.getRoleName() && !Objects.equals(reservation.getUser().getUserId(), requestUser.getUserId())) {
            throw new IllegalStateException("要求されたキャンセルは許可できません。");
        }
        reservationRepository.delete(reservation);
    }

    public List<Reservation> searchReservation(ReservableRoomId reservableRoomId) {
        return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId);
    }
}
