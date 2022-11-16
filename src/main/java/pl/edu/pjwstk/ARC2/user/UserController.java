package pl.edu.pjwstk.ARC2.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/setUserData")
    public HttpStatus setUserData( @RequestBody UserRequest user) {
        if (userService.setUserData(user.getUsername(), user.getLocationX(), user.getLocationY())) return HttpStatus.OK;
        return HttpStatus.BAD_REQUEST;
    }

    @GetMapping("/getUsersList")
    public List<User> getUsersLists() {
        return userService.getUsersList();
    }

    @GetMapping("/getUserData/{username}")
    public User getUserData(@PathVariable(value = "username") String username) {
        return userService.getUserData(username);
    }

}
