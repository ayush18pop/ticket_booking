package ticket_book.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket_book.entities.Train;
import ticket_book.entities.User;
import ticket_book.util.UserServiceUtil;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.reflect.Type;
import java.util.*;
import java.io.File;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String path = "app/src/main/java/ticket_book/localDb/users.json";

    public UserBookingService(User user1) throws IOException {

        this.user = user1;
        loadUserListFromFile();

    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    public void loadUserListFromFile() throws IOException {
        File users = new File(path);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
        });

    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName())
                    && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException {
        objectMapper.writeValue(new File(path), userList);

    }

    public void fetchBookings() {
        Optional<User> foundUser = userList.stream().filter(user1 -> user1.getName().equals(user.getName())).findFirst();
        if (foundUser.isPresent()) {
            foundUser.get().printTickets();
        }
    }

    public Boolean cancelBooking(){

        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        String ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String dest) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        File file = new File("app/src/main/java/ticket_book/localDb/trains.json");
        List<Train> list = mapper.readValue(file, new TypeReference<List<Train>>() {});

        return list.stream().filter(train1 -> {
            return
            train1.getStations().contains(source) && train1.getStations().contains(dest);
        }).toList();
    }





}
