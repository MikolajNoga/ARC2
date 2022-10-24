package pl.edu.pjwstk.ARC2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDataRequest {
    private String name, lastName, location;
    private int id;

    public UserDataRequest(String name, String lastName, String location) {
        this.name = name;
        this.lastName = lastName;
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserDataRequest{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                ", id=" + id +
                '}';
    }
}
