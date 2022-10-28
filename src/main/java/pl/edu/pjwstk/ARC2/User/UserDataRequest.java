package pl.edu.pjwstk.ARC2.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDataRequest {
    private String username, locationX, locationY;

    @Override
    public String toString() {
        return "User{ " + username + " : " + locationX + " : " + locationY + " }";
    }
}
