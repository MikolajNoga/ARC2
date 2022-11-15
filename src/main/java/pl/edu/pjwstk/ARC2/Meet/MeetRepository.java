package pl.edu.pjwstk.ARC2.Meet;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetRepository {
    HttpStatus createMeet(String username, long numberOfParticipants);

    long getNumberOfParticipantsInMeet(String meetId);

    String getListOfParticipantsInMeet(String meetId);

    Meet getMeet(String username);

    long getTotalNumberOfActiveMeets();

    boolean closeMeet(String username);

}
