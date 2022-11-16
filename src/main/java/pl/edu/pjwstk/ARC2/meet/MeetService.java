package pl.edu.pjwstk.ARC2.meet;

import com.google.cloud.datastore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.edu.pjwstk.ARC2.user.User;
import pl.edu.pjwstk.ARC2.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetService implements MeetRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final UserService userService;

    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("meet");

    private QueryResults<Entity> query(String username) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("meet")
                .setFilter(StructuredQuery.PropertyFilter.eq("username", username))
                .build();
        return datastore.run(query);
    }

    /* Jedna forma rozwiązania, problem jest tylko taki, że po dodaniu meet'a, __Stat_Kind__ nie
     zaaktualizuje się od razu, tak jak jest to opisane w dokumentacji. Mimo wszystko jeśli nie zależy nam na bardzo
     dokładnej ilości elementów rozwiązanie dobre */
    private QueryResults<Entity> totalNumberOfMeetsQuery() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("__Stat_Kind__")
                .setFilter(StructuredQuery.PropertyFilter.eq("kind_name", "meet"))
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
                .set("users", StringValue.newBuilder(userAddedToMeet.toString()).build())
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
            return currentEntity.getString("users");
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

    @Override
    public long getTotalNumberOfActiveMeets() {
        QueryResults<Entity> results = totalNumberOfMeetsQuery();
        if (results.hasNext()){
            Entity currentEntity = results.next();
            return currentEntity.getLong("count");
        }
        return -1;
    }

    @Override
    public boolean closeMeet(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()){
            Entity currentEntity = results.next();
            String[] users = currentEntity.getString("users").replaceAll("(\\[|\\])","").split(",");
            for(String temp: users) changeUserMeetAttendance(userService.getUserEntity(temp.trim()));
            datastore.delete(currentEntity.getKey());
            return true;
        }
        return false;
    }


}
