package pl.edu.pjwstk.ARC2.user;

import com.google.cloud.datastore.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserRepository {
    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("user");

    private QueryResults<Entity> query() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("user")
                .build();
        return datastore.run(query);
    }

    private QueryResults<Entity> query(String username) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("user")
                .setFilter(StructuredQuery.PropertyFilter.eq("username", username))
                .build();
        return datastore.run(query);
    }

    private QueryResults<Entity> query(Long locationX, Long locationY) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("user")
                .setFilter(
                        StructuredQuery.CompositeFilter.and(
                                StructuredQuery.PropertyFilter.eq("locationX", locationX),
                                StructuredQuery.PropertyFilter.eq("locationY", locationY),
                                StructuredQuery.PropertyFilter.eq("isSetToMeet",false)
                        )
                )
                .build();
        return datastore.run(query);
    }


    @Override
    public boolean setUserData(String username, String locationX, String locationY) {
        Key key = datastore.allocateId(keyFactory.newKey());

        Entity user = Entity.newBuilder(key)
                .set(
                        "username",
                        StringValue.newBuilder(username).build())
                .set(
                        "locationX",
                        LongValue.newBuilder(Long.parseLong(locationX)).build())
                .set(
                        "locationY",
                        LongValue.newBuilder(Long.parseLong(locationY)).build())
                .set(
                        "isSetToMeet",
                        BooleanValue.newBuilder(false).build()
                )
                .build();
        datastore.put(user);
        return true;
    }

    @Override
    public User getUserData(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()){
            Entity currentEntity = results.next();
            return new User(
                    currentEntity.getString("username"),
                    currentEntity.getLong("locationX"),
                    currentEntity.getLong("locationY"),
                    currentEntity.getBoolean("isSetToMeet"));
        }
        return null;
    }

    public Entity getUserEntity(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()){
            return results.next();
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
                    currentEntity.getLong("locationX"),
                    currentEntity.getLong("locationY"),
                    currentEntity.getBoolean("isSetToMeet")));
        }
        return listOfEntities;
    }

    public List<User> getUsersList(Long locationX, Long locationY) {
        List<User> listOfEntities = new ArrayList<>();
        QueryResults<Entity> results = query(locationX, locationY);
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new User(
                    currentEntity.getString("username"),
                    currentEntity.getLong("locationX"),
                    currentEntity.getLong("locationY"),
                    currentEntity.getBoolean("isSetToMeet")));
        }
        return listOfEntities;
    }
}
