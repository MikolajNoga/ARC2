package pl.edu.pjwstk.ARC2.User;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/setUserData/{username}/{locationX}/{locationY}")
    public HttpStatus setUserData(
            @PathVariable(value = "username") String username,
            @PathVariable(value = "locationX") String locationX,
            @PathVariable(value = "locationY") String locationY) {
        if (userService.setUserData(username, locationX, locationY)) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
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
