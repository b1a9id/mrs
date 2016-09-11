package mrs.app.room;

import mrs.domain.model.ReservableRoom;
import mrs.domain.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomSearchController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public String search(Model model) {
        LocalDate today = LocalDate.now();
        List<ReservableRoom> rooms = roomService.searchReservableRooms(today);
        model.addAttribute("date", today);
        model.addAttribute("rooms", rooms);
        return "room/list-rooms";
    }

    @GetMapping(value = "/{date}")
    public String search(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @PathVariable LocalDate date,
            Model model) {
        List<ReservableRoom> rooms = roomService.searchReservableRooms(date);
        model.addAttribute("date", date);
        model.addAttribute("rooms", rooms);
        return "room/list-rooms";
    }
}
