package pl.edu.pjwstk.ARC2.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRequest {
    private String username;
    private MultipartFile file;
    private String locationX;
    private String locationY;
}
