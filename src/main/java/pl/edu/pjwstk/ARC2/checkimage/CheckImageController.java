package pl.edu.pjwstk.ARC2.checkimage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

@RestController
@AllArgsConstructor
public class CheckImageController {

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/onNewImage")
    public HttpStatus checkImage(HttpServletRequest req) throws IOException {
        JsonNode bodyNode = objectMapper.readTree(req.getReader());

        String base64Data = bodyNode.get("message")
                .get("attributes").get("objectId").asText();
        System.out.println(base64Data);
        return HttpStatus.OK;
    }
}
