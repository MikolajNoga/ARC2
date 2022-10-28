package pl.edu.pjwstk.ARC2.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    boolean setUserData(String username, String locationX, String locationY);
    String getUserData(String username);
    String getUsersList();
}
