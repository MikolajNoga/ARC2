package pl.edu.pjwstk.ARC2.meet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Http;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjwstk.ARC2.checkimage.visionapi.DetectLabelsGcs;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@AllArgsConstructor
public class MeetController {

    private final MeetService meetService;
    private final ObjectMapper objectMapper;

    @PostMapping("/createMeet")
    public HttpStatus createMeet(@RequestBody MeetRequest request) {
        return meetService.createMeet(request.getUsername(), Long.parseLong(request.getNumberOfParticipants()));
    }
    @PostMapping(
            value = "/addUserToClosestMeet",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus addNewUserToMeetIfAvailable(HttpServletRequest req) throws IOException {
        JsonNode bodyNode = objectMapper.readTree(req.getReader());

        String base64Data = bodyNode.get("message").asText();
        meetService.addUserToClosestMeet(base64Data);

        return HttpStatus.OK;
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
