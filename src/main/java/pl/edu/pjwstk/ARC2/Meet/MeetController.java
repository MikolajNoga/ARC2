package pl.edu.pjwstk.ARC2.Meet;

import com.google.cloud.datastore.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MeetController {

    private final MeetService meetService;

    @GetMapping("/createMeet/{username}/{numberOfParticipants}/{range}")
    public HttpStatus createMeet(
            @PathVariable String numberOfParticipants,
            @PathVariable String range,
            @PathVariable String username) {
        return meetService.createMeet(username, Integer.parseInt(numberOfParticipants), Double.parseDouble(range));
    }

    @GetMapping("/getMeet/{username}")
    public String getMeet(@PathVariable(value = "username") String username){
        return meetService.getMeet(username).toString();
    }

}
