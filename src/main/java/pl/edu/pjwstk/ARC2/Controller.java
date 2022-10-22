package pl.edu.pjwstk.ARC2;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {

    @GetMapping("/setData")
    public void setUserData(){
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String kind = "Task";
        // The name/ID for the new entity
        String name = "sampletask1";
        // The Cloud Datastore key for the new entity
        Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);
        Entity task = Entity.newBuilder(taskKey).set("description", "Buy milk").build();

        datastore.put(task);
        System.out.printf("Saved %s: %s%n", task.getKey().getName(), task.getString("description"));

        Entity retrieved = datastore.get(taskKey);
        System.out.printf("Retrieved %s: %s%n", taskKey.getName(), retrieved.getString("description"));
    }
}
