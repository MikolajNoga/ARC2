package pl.edu.pjwstk.ARC2.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/setUserData/{username}/{locationX}/{locationY}")
    public HttpStatus setUserData( @RequestBody UserRequest user) {
        if (userService.setUserData(user.getUsername(), user.getLocationX(), user.getLocationY())) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;

//        @PathVariable(value = "username") String username,
//        @PathVariable(value = "locationX") String locationX,
//        @PathVariable(value = "locationY") String locationY
    }

    @GetMapping("/getUsersList")
    public List<User> getUsersLists() {
        return userService.getUsersList();
    }

    @PostMapping("/getUserData")
    public User getUserData(@RequestBody String username) {
        return userService.getUserData(username);
    }

}
