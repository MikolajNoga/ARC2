package pl.edu.pjwstk.ARC2.user;

import com.google.cloud.datastore.*;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pjwstk.ARC2.bigquery.TableInsertRows;
import pl.edu.pjwstk.ARC2.cloudstorage.UploadObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public boolean setUserData(String username, MultipartFile file ,String locationX, String locationY) throws IOException {
        Key key = datastore.allocateId(keyFactory.newKey());

        UploadObject.uploadObjectFromMemory(
                "project-arc2",
                "project-arc2.appspot.com",
                "userPhotos/" + username,
                file.getBytes(),
                file.getContentType());

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
                        BooleanValue.newBuilder(false).build())
                .build();
        datastore.put(user);

        sendRowToBigQuery(username,locationX, locationY,false);

        return true;
    }

    public boolean register(String username, MultipartFile file, String locationX, String locationY) throws IOException {
        // Create a new user entity and set its properties
        Key key = datastore.allocateId(keyFactory.newKey());
        UploadObject.uploadObjectFromMemory(
                "project-arc2",
                "project-arc2.appspot.com",
                "userPhotos/" + username,
                file.getBytes(),
                file.getContentType());

        Entity user = Entity.newBuilder(key)
                .set("username", StringValue.newBuilder(username).build())
                .set("locationX", LongValue.newBuilder(Long.parseLong(locationX)).build())
                .set("locationY", LongValue.newBuilder(Long.parseLong(locationY)).build())
                .set("isSetToMeet", BooleanValue.newBuilder(false).build())
                .build();
        datastore.put(user);

        // Send a message to Pub/Sub with the new user data
        Map<String, String> data = new HashMap<>();
        data.put("type", "new");
        data.put("isSetToMeet", "false");
        data.put("username", username);
        data.put("locationX", locationX);
        data.put("locationY", locationY);
        sendMessageToPubSub(data);
        return true;
    }

    private void sendMessageToPubSub(Map<String, String> data) throws IOException {
        // Create a new publisher client
        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder("projects/project-arc2/topics/new-user")
                    .build();

            // Convert the data map to a JSON string
            String jsonData = new Gson().toJson(data);

            // Send the message to Pub/Sub
            ByteString messageBytes = ByteString.copyFromUtf8(jsonData);
            PubsubMessage message = PubsubMessage.newBuilder().setData(messageBytes).build();
            publisher.publish(message);
        } finally {
            // Shut down the publisher
            if (publisher != null) {
                publisher.shutdown();
            }
        }
    }




    private static void sendRowToBigQuery(String username, String locationX, String locationY, Boolean isSetToMeet){
        String datasetName = "DataSet";
        String tableName = "Users";

        Map<String, Object> rowContent = new HashMap<>();
        rowContent.put("isSetToMeet", isSetToMeet);
        rowContent.put("locationX", Long.parseLong(locationX));
        rowContent.put("locationY", Long.parseLong(locationY));
        rowContent.put("username", username);

        TableInsertRows.tableInsertRows(datasetName, tableName, rowContent);
    }

    @Override
    public User getUserData(String username) {
        QueryResults<Entity> results = query(username);
        if (results.hasNext()){
            Entity currentEntity = results.next();
            return new User(
                    currentEntity.getString("username"),
                    currentEntity.getString("photoUrl"),
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
                    currentEntity.getString("photoUrl"),
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
                    currentEntity.getString("photoUrl"),
                    currentEntity.getLong("locationX"),
                    currentEntity.getLong("locationY"),
                    currentEntity.getBoolean("isSetToMeet")));
        }
        return listOfEntities;
    }
}
