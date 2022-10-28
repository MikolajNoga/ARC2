package pl.edu.pjwstk.ARC2.User;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/setUserData/{username}/{locationX}/{locationY}")
    public HttpStatus setUserData(
            @PathVariable(value = "username") String username,
            @PathVariable(value = "locationX") String locationX,
            @PathVariable(value = "locationY") String locationY){
        if (userService.setUserData(username,locationX,locationY))
            return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @GetMapping("/getUsersList")
    public String getUsersLists(){
        return userService.getUsersList();
    }


}
