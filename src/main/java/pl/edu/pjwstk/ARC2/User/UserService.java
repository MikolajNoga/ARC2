package pl.edu.pjwstk.ARC2.User;

import com.google.cloud.datastore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserRepository{
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("user");

    private QueryResults<Entity> query(){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("user")
                .build();
        return datastore.run(query);
    }

    @Override
    public boolean setUserData(String username, String locationX, String locationY) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity user = Entity.newBuilder(key)
                .set(
                        "username",
                        StringValue.newBuilder(username).setExcludeFromIndexes(true).build())
                .set(
                        "locationX",
                        StringValue.newBuilder(locationX).setExcludeFromIndexes(true).build())
                .set(
                        "locationY",
                        StringValue.newBuilder(locationY).setExcludeFromIndexes(true).build())
                .set(
                        "isSetToMeet",
                        BooleanValue.newBuilder(false).setExcludeFromIndexes(true).build()
                        ).build();
        datastore.put(user);
        return true;
    }

    @Override
    public User getUserData(String username) {
        QueryResults<Entity> results = query();
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("username").equals(username)){
                return new User(
                        currentEntity.getString("username"),
                        currentEntity.getString("locationX"),
                        currentEntity.getString("locationX"),
                        currentEntity.getBoolean("isSetToMeet"));
            }
        }
        return null;
    }

    @Override
    public List<User> getUsersList() {
        List<User> listOfEntities = new ArrayList<>();
        QueryResults<Entity> results = query();
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new User(
                    currentEntity.getString("username"),
                    currentEntity.getString("locationX"),
                    currentEntity.getString("locationX"),
                    currentEntity.getBoolean("isSetToMeet")));
        }
        return listOfEntities;
    }
}
