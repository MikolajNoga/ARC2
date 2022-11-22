package pl.edu.pjwstk.ARC2.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String photoUrl;
    private String locationX;
    private String locationY;
}
