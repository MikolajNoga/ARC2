package pl.edu.pjwstk.ARC2.Meet;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetRepository {
    HttpStatus createMeet(String username, int numberOfParticipants, double range);

    int getNumberOfParticipantsInMeet(String meetId);

    List<Value<?>> getListOfParticipantsInMeet(String meetId);

    Entity getMeet(String username);

}
