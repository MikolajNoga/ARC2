package pl.edu.pjwstk.ARC2.checkimage;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CheckImageController {

    @PostMapping("/onNewImage")
    public HttpStatus checkImage(@RequestBody JsonObject object){
        System.out.println(object.get("data"));
        System.out.println(object);
        return HttpStatus.OK;
    }
}
