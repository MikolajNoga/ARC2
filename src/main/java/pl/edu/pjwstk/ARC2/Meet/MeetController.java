package pl.edu.pjwstk.ARC2.Meet;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MeetController {

    private final MeetService meetService;

    @GetMapping("/createMeet/{username}/{numberOfParticipants}/{range}")
    public String createMeet(
            @PathVariable String numberOfParticipants,
            @PathVariable String range,
            @PathVariable String username) {
        return meetService.createMeet(username, Integer.parseInt(numberOfParticipants), Double.parseDouble(range));
    }
}
