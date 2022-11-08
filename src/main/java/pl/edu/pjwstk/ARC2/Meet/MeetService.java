package pl.edu.pjwstk.ARC2.Meet;

import com.google.cloud.datastore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.User.User;
import pl.edu.pjwstk.ARC2.User.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetService implements MeetRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final UserService userService;

    private QueryResults<Entity> query() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("meet")
                .build();
        return datastore.run(query);
    }

    private void setUserMeetAttendance(Entity entity){
        if (entity != null) {
            entity = Entity.newBuilder(entity)
                    .set("isSetToMeet", true)
                    .build();
            datastore.update(entity);
        }
    }


    private boolean isDistanceCloseEnough(double range, int x1, int y1, int x2, int y2) {
        return Math.sqrt((Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1), 2.0))) <= range;
    }

    @Override
    public HttpStatus createMeet(String username, int numberOfParticipants, double range) {
        User user = userService.getUserData(username);
        if (!user.isSetToMeet()) return HttpStatus.BAD_REQUEST;
        List<User> userAddedToMeet = new ArrayList<>();
        userAddedToMeet.add(user);
        setUserMeetAttendance(userService.getUserEntity(username));
        user.setSetToMeet(true);

        List<User> allUsers = new ArrayList<>();

        for (int x = -1; x < 2; x++)
            for (int y = -1; y < 2; y++){
                allUsers.addAll(userService.getUsersList(String.valueOf(user.getIntVersionOfLocationX()+x),
                        String.valueOf(user.getIntVersionOfLocationY()+y)));
            }


        // nop is number of participants which track number of added to meet in for loop
        for (int i = 0, nop = 1; i < allUsers.size(); i++) {
            if (nop >= numberOfParticipants) break;
            if (!userAddedToMeet.contains(allUsers.get(i)) && !allUsers.get(i).isSetToMeet() &&
                    isDistanceCloseEnough(range, user.getIntVersionOfLocationX(), user.getIntVersionOfLocationY(),
                            allUsers.get(i).getIntVersionOfLocationX(), allUsers.get(i).getIntVersionOfLocationY())) {
                userAddedToMeet.add(allUsers.get(i));
                nop++;
            }
        }

        for (User value : userAddedToMeet) {
            Entity entity = userService.getUserEntity(value.getUsername());
            if (!entity.getString("username").equals(username)) {
                setUserMeetAttendance(entity);
                value.setSetToMeet(true);
            }
        }

        Key taskKey1 =
                datastore
                        .newKeyFactory()
                        .addAncestors(PathElement.of("user", userAddedToMeet.toString()))
                        .setKind("meet")
                        .newKey("newMeet");

        Entity meet = Entity.newBuilder(taskKey1)
                .set(
                        "username",
                        StringValue.newBuilder(username).build())
                .set(
                        "numberOfParticipants",
                        StringValue.newBuilder(String.valueOf(numberOfParticipants)).build())
                .set(
                        "range",
                        StringValue.newBuilder(String.valueOf(range)).build())
                .build();
        datastore.put(meet);

        return HttpStatus.OK;
    }

    @Override
    public String getNumberOfParticipantsInMeet(String username) {
        QueryResults<Entity> results = query();
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            if (currentEntity.getString("username").equals(username))
                return currentEntity.getString("numberOfParticipants");
        }
        return null;
    }


    @Override
    public String getListOfParticipantsInMeet(String username) {
        QueryResults<Entity> results = query();
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            if (currentEntity.getString("username").equals(username))
                return currentEntity.getKey().getAncestors().get(0).getName();
        }
        return null;
    }

    @Override
    public Meet getMeet(String username) {
        QueryResults<Entity> results = query();
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            if (currentEntity.getString("username").equals(username)) {
                return new Meet(
                        currentEntity.getKey().getAncestors().get(0).getName(),
                        currentEntity.getString("username"),
                        currentEntity.getString("numberOfParticipants"),
                        currentEntity.getString("range"));
            }
        }
        return null;
    }


}
