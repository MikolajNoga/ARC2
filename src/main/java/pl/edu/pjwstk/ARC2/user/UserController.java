package pl.edu.pjwstk.ARC2.user;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/setUserData", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public HttpStatus setUserData(@RequestBody UserRequest user) throws IOException {
        if (userService.setUserData(
                user.getUsername(), user.getFile(), user.getLocationX(), user.getLocationY())) return HttpStatus.OK;
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
