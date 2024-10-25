package Expense.event.project.Controller;

import Expense.event.project.Entity.User;
import Expense.event.project.Service.Userser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
//@CrossOrigin("http://localhost:5176/")
@RestController
@RequestMapping("/api/v1/users")

public class UserController {


    @Autowired
    private Userser userser;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userser.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        try {
            User user = userser.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID: " + id);
            }
        }
        catch (Exception e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching the user.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        System.out.println("Received user: " + user);
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter a proper email address.");
        }
        System.out.println("User email: " + user.getEmail());
        if (userser.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists.");
        }
        String trimmedRole = user.getRole() != null ? user.getRole().trim() : "";
        List<String> validRoles = Arrays.asList("organizer", "user","Admin");
        if (!validRoles.contains(trimmedRole)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter a valid role.");
        }
        User createdUser = userser.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registered successfully: " + createdUser.getName());

    }

    @PostMapping("/Login")
    public ResponseEntity<String>    Login(@RequestBody User loginRequest) {
        String response =userser.authenticate(loginRequest.getName(), loginRequest.getPassword());
        if ("success".equals(response)) {
            return ResponseEntity.ok("Login successful");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login data");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        String responseMessage = userser.updateUser(id, updatedUser);
        if (responseMessage != null) {
            return ResponseEntity.ok(responseMessage);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userser.deleteUser(id);
            return ResponseEntity.ok("User record deleted successfully.");
        }
        catch (Exception e) {
            System.err.println("Error in deleteUser: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user.");
        }
    }
}
