package pl.edu.pjwstk.ARC2.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String username, locationX, locationY;
    private boolean isSetToMeet;

    @Override
    public String toString() {
        return "User{ " + username + " : " + locationX + " : " + locationY + " : " + isSetToMeet + " }";
    }

    public int getIntVersionOfLocationX() {
        return Integer.parseInt(locationX);
    }

    public int getIntVersionOfLocationY() {
        return Integer.parseInt(locationY);
    }
}
