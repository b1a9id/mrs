package mrs.app.reservation;

import mrs.domain.model.*;
import mrs.domain.service.reservation.ReservationService;
import mrs.domain.service.room.RoomService;
import mrs.domain.service.user.ReservationUserDetails;
import mrs.support.AlreadyReservedExceptiom;
import mrs.support.UnavailableReservationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Controller
@RequestMapping("reservations/{date}/{roomId}")
public class ReservationCreateController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    @ModelAttribute
    public ReservationForm setupForm() {
        ReservationForm form = new ReservationForm();
        form.setStartTime(LocalTime.of(9, 0));
        form.setEndTime(LocalTime.of(10, 0));
        return form;
    }

    @GetMapping
    public String init(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable LocalDate date,
            @PathVariable Integer roomId,
            Model model) {
        ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);
        List<Reservation> reservations = reservationService.searchReservation(reservableRoomId);

        LocalTime baseTime = LocalTime.of(0, 0);
        List<LocalTime> timeList = IntStream.range(0, 24 * 2)
                .mapToObj(i -> baseTime.plusMinutes(30 * i)).collect(Collectors.toList());
        model.addAttribute("room", roomService.searchMeetingRoom(roomId));
        model.addAttribute("reservations", reservations);
        model.addAttribute("timeList", timeList);
//        model.addAttribute("user", dummyUser());
        return "reservation/new";
    }


    @PostMapping
    public String create(@Validated ReservationForm form,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal ReservationUserDetails userDetails,
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable LocalDate date,
                         @PathVariable Integer roomId,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return init(date, roomId, model);
        }

        ReservableRoom reservableRoom = new ReservableRoom(new ReservableRoomId(roomId, date));
        Reservation reservation = new Reservation();
        reservation.setStartTime(form.getStartTime());
        reservation.setEndTime(form.getEndTime());
        reservation.setReservableRoom(reservableRoom);
        reservation.setUser(userDetails.getUser());

        try {
            reservationService.reserve(reservation);
        } catch (UnavailableReservationException | AlreadyReservedExceptiom e) {
            model.addAttribute("error", e.getMessage());
            return init(date, roomId, model);
        }
        return "redirect:/reservations/{date}/{roomId}";
    }

    @PostMapping(params = "cancel")
    public String cancel(
            @AuthenticationPrincipal ReservationUserDetails userDetails,
            @RequestParam Integer reservationId,
            @PathVariable Integer roomId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable LocalDate date,
            Model model) {
        User user = userDetails.getUser();
        try {
            reservationService.cancel(reservationId, user);
        } catch (AccessDeniedException e) {
            model.addAttribute("error", e.getMessage());
            return init(date, roomId, model);
        }
        return "redirect:/reservations/{date}/{roomId}";
    }

    private User dummyUser() {
        User user = new User();
        user.setUserId("taro-yamada");
        user.setFirstName("太郎");
        user.setLastName("山田");
        user.setRoleName(RoleName.USER);
        return user;
    }
}
