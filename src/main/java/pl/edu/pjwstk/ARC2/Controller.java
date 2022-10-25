package pl.edu.pjwstk.ARC2;

import com.google.cloud.datastore.*;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class Controller {

    private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    // Create a Key factory to construct keys associated with this project.
    private final KeyFactory keyFactory = datastore.newKeyFactory().setKind("user");

    @GetMapping("/")
    public String helloPage() {
        return "Hello";
    }

    @GetMapping("/getUserData")
    public String getUserData() {
        List<UserDataRequest> listOfEntities = new ArrayList<>();
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("user")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            listOfEntities.add(new UserDataRequest(currentEntity.getString("firstName"), currentEntity.getString("lastName"), currentEntity.getString("Location")));
        }
        return listOfEntities.toString();
    }

    @GetMapping("/getUsersFirstName")
    public String getUsersFirstName() {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("user")
                .build();
        QueryResults<Entity> results = datastore.run(query);
        String str = "";
        while (results.hasNext()) {
            Entity currentEntity = results.next();
            str += currentEntity.getString("firstName") + ", ";
        }
        return str;
    }





//    @PostMapping("/setUserData")
//    public ResponseEntity<Entity> setUserData(UserDataRequest request) {
//        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
//        String kind = "User";
//
//        Key key = datastore.newKeyFactory().setKind(kind).newKey(request.getId());
//        Entity user = Entity.newBuilder(key)
//                .set("name", request.getName())
//                .set("lastName", request.getLastName())
//                .set("location", request.getLocation()).build();
//        datastore.put(user);
//        return ResponseEntity.status(HttpStatus.OK).body(datastore.get(key));
//    }

    // PostMapping -> 405 lub 500 w postman


    @GetMapping("/setUserData/{firstName}/{lastName}/{Location}")
    public Key setUserData(@PathVariable(value = "firstName") String firstName, @PathVariable(value = "lastName") String lastName, @PathVariable(value = "Location") String location) {
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity user = Entity.newBuilder(key)
                .set(
                        "firstName",
                        StringValue.newBuilder(firstName).setExcludeFromIndexes(true).build())
                .set(
                        "lastName",
                        StringValue.newBuilder(lastName).setExcludeFromIndexes(true).build())
                .set(
                        "Location",
                        StringValue.newBuilder(location).setExcludeFromIndexes(true).build())
                .build();
        datastore.put(user);
        return key;
    }
}
