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

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("meet");

    private QueryResults<Entity> query() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("meet")
                .build();
        return datastore.run(query);
    }

    private QueryResults<Entity> query(String username) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("meet")
                .setFilter(StructuredQuery.PropertyFilter.eq("username", username))
                .build();
        return datastore.run(query);
    }

    private void changeUserMeetAttendance(Entity entity) {
        if (entity != null) {
            entity = Entity.newBuilder(entity)
                    .set("isSetToMeet", !entity.getBoolean("isSetToMeet"))
                    .build();
            datastore.update(entity);
        }
    }


    private boolean isDistanceCloseEnough(double range, int x1, int y1, int x2, int y2) {
        return Math.sqrt((Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1), 2.0))) <= range;
    }

    @Override
    public HttpStatus createMeet(String username, long numberOfParticipants) {
        User user = userService.getUserData(username);
        if (user.isSetToMeet()) return HttpStatus.BAD_REQUEST;

        List<String> userAddedToMeet = new ArrayList<>();
        userAddedToMeet.add(user.getUsername());
        changeUserMeetAttendance(userService.getUserEntity(username));
        user.setSetToMeet(true);

        List<User> allUsers = new ArrayList<>();

        for (int x = -1; x < 2; x++)
            for (int y = -1; y < 2; y++) {
                allUsers.addAll(userService.getUsersList(
                        user.getLocationX() + x,
                        user.getLocationY() + y));
            }


        // nop is number of participants which track number of added to meet in for loop
        for (int i = 0, nop = 1; i < allUsers.size(); i++) {
            if (nop >= numberOfParticipants) break;
            if (!userAddedToMeet.contains(allUsers.get(i).getUsername()) && !allUsers.get(i).isSetToMeet()) {
                userAddedToMeet.add(allUsers.get(i).getUsername());
                nop++;
            }
        }

        for (String value : userAddedToMeet) {
            Entity entity = userService.getUserEntity(value);
            if (!entity.getString("username").equals(username)) {
                changeUserMeetAttendance(entity);
            }
        }

        Key key = datastore.allocateId(keyFactory.newKey());

        Entity meet = Entity.newBuilder(key)
                .set("username", StringValue.newBuilder(username).build())
                .set("numberOfParticipants", LongValue.newBuilder(numberOfParticipants).build())
                .set("users", String.valueOf(StringValue.newBuilder(userAddedToMeet.toString())))
                .build();
        datastore.put(meet);

        return HttpStatus.OK;
    }

    @Override
    public long getNumberOfParticipantsInMeet(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()) {
            Entity currentEntity = results.next();
            return currentEntity.getLong("numberOfParticipants");
        }
        return -1;
    }


    @Override
    public String getListOfParticipantsInMeet(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()) {
            Entity currentEntity = results.next();
            return currentEntity.getKey().getAncestors().get(0).getName();
        }
        return null;
    }

    @Override
    public Meet getMeet(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()) {
            Entity currentEntity = results.next();
            return new Meet(
                    currentEntity.getString("users"),
                    currentEntity.getString("username"),
                    currentEntity.getLong("numberOfParticipants"));
        }
        return null;
    }

}
