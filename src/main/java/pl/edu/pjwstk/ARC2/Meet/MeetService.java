package pl.edu.pjwstk.ARC2.Meet;

import com.google.cloud.datastore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.User.User;
import pl.edu.pjwstk.ARC2.User.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetService implements MeetRepository{
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("meet");
    private final UserService userService;

    private QueryResults<Entity> query(){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("meet")
                .build();
        return datastore.run(query);
    }

    private boolean isDistanceCloseEnough(double range, int x1, int y1, int x2, int y2){
        return Math.sqrt((Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1), 2.0))) <= range;
    }

    @Override
    public String createMeet(String username, int numberOfParticipants, double range) {
        User user = userService.getUserData(username);
        List<User> userAddedToMeet = new ArrayList<>();
        userAddedToMeet.add(user);

        List<User> allUsers = userService.getUsersList();
        // nop is number of participants which track number of added to meet in for loop
        for (int i = 0 , nop = 1; i < allUsers.size(); i++){
            if (nop >= numberOfParticipants) break;
            if (!userAddedToMeet.contains(allUsers.get(i)) &&
                    isDistanceCloseEnough(range, user.getIntVersionOfLocationX(), user.getIntVersionOfLocationY(),
                            allUsers.get(i).getIntVersionOfLocationX(), allUsers.get(i).getIntVersionOfLocationY())){
                userAddedToMeet.add(allUsers.get(i));
                nop++;
            }
        }

        // TODO: 30.10.2022 Dodanie meeta w którym znajdzie się liczba członków (userAddedToMeet.size())
        //  oraz kto tam jest na bazie usernameów, oraz update danych w użykownikach jako isSetToMeet na true.
        //  Meet musi posiadać jakieś unikatowe id.
        //  Oraz użytkownikcy zapisani jako lista [getListOfParticipantsInMeet używa lisy więc przy innym roziązaniu do zmiany tamta metoda]

        Key key = datastore.allocateId(keyFactory.newKey());

        Key taskKey1 =
                datastore
                        .newKeyFactory()
                        .addAncestors(PathElement.of("TaskList", "default"))
                        .setKind("Task")
                        .newKey("sampleTask");

        KeyFactory keyFactory =
                datastore
                        .newKeyFactory()
                        .addAncestors(PathElement.of("User", "Alice"), PathElement.of("TaskList", "default"))
                        .setKind("Task");
        Key taskKey = keyFactory.newKey("sampleTask");
        return null;
    }

    @Override
    public int getNumberOfParticipantsInMeet(String meetId) {
        return getListOfParticipantsInMeet(meetId).size();
    }


    @Override
    public List<Value<?>> getListOfParticipantsInMeet(String meetId) {
        QueryResults<Entity> results = query();
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("id").equals(meetId))
                return currentEntity.getList("participants");
        }
        return null;
    }
}
