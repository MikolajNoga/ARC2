package pl.edu.pjwstk.ARC2.Meet;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetRepository {
    HttpStatus createMeet(String username, int numberOfParticipants, double range);

    String getNumberOfParticipantsInMeet(String meetId);

    String getListOfParticipantsInMeet(String meetId);

    Meet getMeet(String username);

}
