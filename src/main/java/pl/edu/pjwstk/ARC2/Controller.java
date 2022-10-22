package pl.edu.pjwstk.ARC2;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
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

    @GetMapping("/")
    public String helloPage() {
        return "Hello";
    }

    @GetMapping("/getUserData/{id}")
    public Entity getUserData(@PathVariable("id") String id) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        return datastore.get(datastore.newKeyFactory().setKind("user").newKey(id));
    }

    @PostMapping("/setUserData")
    public ResponseEntity<Entity> setUserData(UserDataRequest request) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String kind = "User";

        Key key = datastore.newKeyFactory().setKind(kind).newKey(request.getId());
        Entity user = Entity.newBuilder(key)
                .set("name", request.getName())
                .set("lastName", request.getLastName())
                .set("location", request.getLocation()).build();
        datastore.put(user);
        return ResponseEntity.status(HttpStatus.OK).body(datastore.get(key));
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
