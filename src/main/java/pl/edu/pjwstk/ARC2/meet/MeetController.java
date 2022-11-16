package pl.edu.pjwstk.ARC2.meet;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class MeetController {

    private final MeetService meetService;

    @PostMapping("/createMeet")
    public HttpStatus createMeet(@RequestBody MeetRequest request) {
        return meetService.createMeet(request.getUsername(), Long.parseLong(request.getNumberOfParticipants()));
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

    @DeleteMapping("/closeMeet/{username}")
    public HttpStatus closeMeet(@PathVariable(value = "username") String username){
        if (meetService.closeMeet(username)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

}
