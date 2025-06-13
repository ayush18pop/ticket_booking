package ticket_book.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_book.entities.User;
import ticket_book.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;
import java.io.File;


public class UserBookingService {

private User user;
private List<User>  userList;
private ObjectMapper objectMapper = new ObjectMapper();

private static final String path = "app/src/main/java/ticket_book/localDb/users.json";
public UserBookingService(User user1) throws IOException {

    this.user  = user1;
    loadUserListFromFile();

}

public UserBookingService() throws IOException {
    loadUserListFromFile();
}

public void loadUserListFromFile() throws IOException {
    File users = new File(path);
    userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});

}
    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    

}
