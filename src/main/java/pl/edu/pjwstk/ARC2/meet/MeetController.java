package pl.edu.pjwstk.ARC2.meet;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MeetController {

    private final MeetService meetService;

    @GetMapping("/createMeet/{username}/{numberOfParticipants}")
    public HttpStatus createMeet(
            @PathVariable String numberOfParticipants,
            @PathVariable String username) {
        return meetService.createMeet(username, Long.parseLong(numberOfParticipants));
    }

    @GetMapping("/getMeet/{username}")
    public String getMeet(@PathVariable(value = "username") String username){
        return meetService.getMeet(username).toString();
    }

    @GetMapping("/getNumberOfParticipants/{username}")
    public Long getNumberOfParticipants(@PathVariable(value = "username") String username){
        return meetService.getNumberOfParticipantsInMeet(username);
    }

    @GetMapping("/getListOfUsersInMeet/{username}")
    public String getListOfUsersInMeet(@PathVariable(value = "username") String username){
        return meetService.getListOfParticipantsInMeet(username);
    }

    @GetMapping("/getTotalNumberOfActiveMeets")
    public long getTotalNumberOfActiveMeets(){
        return meetService.getTotalNumberOfActiveMeets();
    }

    @GetMapping("/closeMeet/{username}")
    public HttpStatus closeMeet(@PathVariable(value = "username") String username){
        if (meetService.closeMeet(username)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

}
