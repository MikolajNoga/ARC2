package pl.edu.pjwstk.ARC2.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String username;
    private Long locationX, locationY;
    private boolean isSetToMeet;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", isSetToMeet=" + isSetToMeet +
                '}';
    }
}
