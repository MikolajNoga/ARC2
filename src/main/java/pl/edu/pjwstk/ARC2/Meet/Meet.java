package pl.edu.pjwstk.ARC2.Meet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Meet {
    private String users;
    private String mainUser;
    private long numberOfParticipants;

    @Override
    public String toString() {
        return "Meet{" + "users=" + users + ", mainUser='" + mainUser +
                ", numberOfParticipants=" + numberOfParticipants + '}';
    }

}
