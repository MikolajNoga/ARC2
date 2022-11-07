package pl.edu.pjwstk.ARC2.User;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    boolean setUserData(String username, String locationX, String locationY);

    User getUserData(String username);

    List<User> getUsersList();
}
