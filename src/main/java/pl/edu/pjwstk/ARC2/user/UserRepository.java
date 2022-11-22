package pl.edu.pjwstk.ARC2.user;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Repository
public interface UserRepository {
    boolean setUserData(String username, MultipartFile file,String locationX, String locationY) throws IOException;

    User getUserData(String username);

    List<User> getUsersList();
}
