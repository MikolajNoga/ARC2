package pl.edu.pjwstk.ARC2.Meet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Meet {
    private List<String> users;
    private String mainUser;
    private int numberOfParticipants;
    private double range;

    @Override
    public String toString() {
        return "Meet{" + "users=" + users + ", mainUser='" + mainUser +
                ", numberOfParticipants=" + numberOfParticipants +
                ", range=" + range +
                '}';
    }

}
