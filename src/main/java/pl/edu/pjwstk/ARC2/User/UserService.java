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

    private QueryResults<Entity> query(String kind){
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(kind)
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
                .build();
        datastore.put(user);
        return true;
    }

    @Override
    public String getUserData(String username) {
        QueryResults<Entity> results = query("user");
        String str = "Nie znaleziono użytkownika";
        while (results.hasNext()){
            Entity currentEntity = results.next();
            if (currentEntity.getString("username").equals(username)){
                str = "{" + currentEntity.getString("username") + ", "
                        + currentEntity.getString("locationX") + ", "
                        + currentEntity.getString("locationX") + "}";
            }
        }
        return str;
    }

    @Override
    public String getUsersList() {
        List<UserDataRequest> listOfEntities = new ArrayList<>();
        QueryResults<Entity> results = query("user");
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new UserDataRequest(
                    currentEntity.getString("username"),
                    currentEntity.getString("locationX"),
                    currentEntity.getString("locationX")));
        }
        return listOfEntities.toString();
    }
}
