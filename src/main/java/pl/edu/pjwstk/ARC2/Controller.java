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

    @GetMapping("/getUserData/{id}")
    public Entity getUserData(@PathVariable("id") Key id) {
//        return datastore.get(datastore.newKeyFactory().setKind("user").newKey(id));
        return datastore.get(id);
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
    public void setUserData(@PathVariable(value = "firstName")  String firstName, @PathVariable(value = "lastName") String lastName, @PathVariable(value = "Location") String location) {
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
    }





//    @GetMapping("/setData")
//    public void setUserData(){
//
//        String kind = "Task";
//        // The name/ID for the new entity
//        String name = "sampletask1";
//        // The Cloud Datastore key for the new entity
//        Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);
//        Entity task = Entity.newBuilder(taskKey).set("description", "Buy milk").build();
//
//        datastore.put(task);
//        System.out.printf("Saved %s: %s%n", task.getKey().getName(), task.getString("description"));
//
//        Entity retrieved = datastore.get(taskKey);
//        System.out.printf("Retrieved %s: %s%n", taskKey.getName(), retrieved.getString("description"));
//    }
}
